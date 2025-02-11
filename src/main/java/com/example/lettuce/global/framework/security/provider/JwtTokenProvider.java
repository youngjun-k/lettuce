package com.example.lettuce.global.framework.security.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.lettuce.domain.user.entity.User;
import com.example.lettuce.global.shared.constant.AuthConstants;
import com.example.lettuce.global.framework.security.principal.UserDetailsImpl;
import com.example.lettuce.global.framework.security.principal.UserDetailsServiceImpl;
import com.example.lettuce.global.shared.exception.CustomJwtException;
import com.example.lettuce.global.shared.exception.InvalidParamException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.*;

@Component
public class JwtTokenProvider {

    public static final Long ACCESS_TOKEN_EXPIRE_TIME = Duration.ofHours(6).toMillis();
    public static final Long EMAIL_VERIFICATION_TOKEN_EXPIRE_TIME = Duration.ofHours(1).toMillis();
    public static final Long RESET_PASSWORD_TOKEN_EXPIRE_TIME = Duration.ofMinutes(30).toMillis();
    
    private final SecretKey key;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenProvider(
            @Value("${spring.jwt.secret}") String key,
            UserDetailsServiceImpl userDetailsService) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userDetailsService = userDetailsService;
    }

    public String createAccessToken(User user) {
        return buildToken(createUserClaims(user), ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String createEmailVerificationToken(String email) {
        return buildToken(createEmailClaims(email), EMAIL_VERIFICATION_TOKEN_EXPIRE_TIME);
    }

    public String createResetPasswordToken(String email) {
        return buildToken(createEmailClaims(email), RESET_PASSWORD_TOKEN_EXPIRE_TIME);
    }

    public Authentication getAuthentication(String token) {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(getEmailByToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String resolveToken(String header) {
        return Optional.ofNullable(header)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.INVALID_AUTHENTICATION))
                .replace(AuthConstants.TOKEN_PREFIX, "");
    }

    public boolean isValidateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        return !getExpirationByToken(token).before(new Date());
    }

    public Long getUserIdFromToken(String token) {
        return parseClaims(token).get("id", Long.class);
    }

    public String getEmailByToken(String token) {
        return parseClaims(token).get("email", String.class);
    }

    public Date getExpirationByToken(String token) {
        return parseClaims(token).getExpiration();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException | SignatureException ex) {
            throw new CustomJwtException(ErrorCode.MALFORMED_JWT_EXCEPTION);
        } catch (ExpiredJwtException ex) {
            throw new CustomJwtException(ErrorCode.EXPIRED_JWT_EXCEPTION);
        } catch (UnsupportedJwtException ex) {
            throw new CustomJwtException(ErrorCode.UNSUPPORTED_JWT_EXCEPTION);
        } catch (IllegalArgumentException ex) {
            throw new CustomJwtException(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

    private HashMap<String, Object> createUserClaims(User user) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", user.getId());
        hashMap.put("email", user.getEmail());
        hashMap.put("role", user.getRole());
        return hashMap;
    }

    private HashMap<String, Object> createEmailClaims(String email) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", email);
        return hashMap;
    }

    public String buildToken(HashMap<String, Object> claims, long expireTime) {
        Date now = new Date();
        long tokenExpireTime = now.getTime() + expireTime;

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(tokenExpireTime))
                .signWith(key)
                .compact();
    }

}