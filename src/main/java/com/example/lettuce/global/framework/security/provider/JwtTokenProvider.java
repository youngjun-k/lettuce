package com.example.lettuce.global.framework.security.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.security.enums.TokenExpireTime;
import com.example.lettuce.global.framework.security.principal.UserDetailsImpl;
import com.example.lettuce.global.framework.security.principal.UserDetailsServiceImpl;
import com.example.lettuce.global.shared.constant.AuthConstants;
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
import java.util.*;

@Component
@Scope("singleton")
public class JwtTokenProvider {

    private static final String PROFILE_PATH = "/api/user/profile";
    private static final String ID_CLAIM = "id";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";

    private final SecretKey key;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenProvider(
            @Value("${spring.jwt.secret}") String key,
            UserDetailsServiceImpl userDetailsService) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key));
        this.userDetailsService = userDetailsService;
    }

    public String createAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>(3);
        claims.put(ID_CLAIM, user.getId());
        claims.put(EMAIL_CLAIM, user.getEmail());
        claims.put(ROLE_CLAIM, user.getRole());
        return buildToken(claims, TokenExpireTime.ACCESS_TOKEN.getExpireTime());
    }

    public String createEmailVerificationToken(String email) {
        return buildToken(Collections.singletonMap(EMAIL_CLAIM, email),
                TokenExpireTime.EMAIL_VERIFICATION_TOKEN.getExpireTime());
    }

    public String createResetPasswordToken(String email) {
        return buildToken(Collections.singletonMap(EMAIL_CLAIM, email),
                TokenExpireTime.RESET_PASSWORD_TOKEN.getExpireTime());
    }

    public Authentication getAuthentication(String token, String path) {
        String email = getEmailByToken(token);
        UserDetailsImpl userDetails = (UserDetailsImpl) (path.startsWith(PROFILE_PATH)
                ? userDetailsService.loadUserWithProfileByEmail(email)
                : userDetailsService.loadUserByUsername(email));

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String resolveToken(String header) {
        return Optional.ofNullable(header)
                .map(h -> h.replace(AuthConstants.TOKEN_PREFIX, ""))
                .orElseThrow(() -> new InvalidParamException(ErrorCode.INVALID_AUTHENTICATION));
    }

    public boolean isValidateToken(String token) {
        return StringUtils.hasText(token) && !getExpirationByToken(token).before(new Date());
    }

    public Long getUserIdFromToken(String token) {
        return parseClaims(token).get(ID_CLAIM, Long.class);
    }

    public String getEmailByToken(String token) {
        return parseClaims(token).get(EMAIL_CLAIM, String.class);
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

    public String buildToken(Map<String, Object> claims, long expireTime) {
        Date now = new Date();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expireTime))
                .signWith(key)
                .compact();
    }

}