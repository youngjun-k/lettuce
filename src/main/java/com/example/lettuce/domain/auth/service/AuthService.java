package com.example.lettuce.domain.auth.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.lettuce.domain.auth.controller.DeleteAccountRequest;
import com.example.lettuce.domain.auth.dto.request.CreateUserRequest;
import com.example.lettuce.domain.auth.dto.request.LoginRequest;
import com.example.lettuce.domain.auth.dto.request.ResetPasswordRequest;
import com.example.lettuce.domain.auth.dto.response.AuthResponse;
import com.example.lettuce.domain.auth.dto.response.TokenResponse;
import com.example.lettuce.domain.user.dto.UserInfo;
import com.example.lettuce.domain.user.entity.Profile;
import com.example.lettuce.domain.user.entity.User;
import com.example.lettuce.domain.user.enums.UserRole;
import com.example.lettuce.domain.user.mapper.ProfileMapper;
import com.example.lettuce.domain.user.mapper.UserMapper;
import com.example.lettuce.domain.user.service.ProfileMapperFactory;
import com.example.lettuce.domain.user.service.UserService;
import com.example.lettuce.global.framework.security.provider.JwtTokenProvider;
import com.example.lettuce.global.shared.constant.AuthConstants;
import com.example.lettuce.global.shared.exception.AuthenticationException;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;

    public AuthResponse login(LoginRequest request) {

        final User user = userService.findByEmail(request.email());

        if (!user.isVerified()) {
            throw new AuthenticationException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        validatePassword(request.password(), user.getPassword());

        return createAuthResponse(user);
    }

    /**
     * Helper method that encapsulates registration logic.
     *
     * @param request       Registration request.
     * @param role          The user role.
     * @param profileMapper Lambda to map the request and user to a profile.
     * @param <T>           Type extending CreateUserRequest.
     * @return AuthResponse containing user info and token.
     */
    @Transactional
    public <T extends CreateUserRequest> void registerUser(T request, UserRole role) {
        String email = request.getEmail();
        userService.isNotRegisteredEmail(email);

        final String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = userMapper.toUser(request, encodedPassword, role);

        @SuppressWarnings("unchecked")
        ProfileMapper<T, ?, ?> profileMapper = (ProfileMapper<T, ?, ?>) ProfileMapperFactory
                .getProfileMapper(role);

        final Profile profile = profileMapper.toProfile(request, user);
        user.setProfile(profile);
        profile.setUser(user);

        userService.saveUser(user);

        eventPublisher.publishEvent(new UserRegistrationEvent(email));
    }

    @Transactional
    public AuthResponse verifyEmail(String token) {
        final String email = jwtTokenProvider.getEmailByToken(token);
        final User user = userService.findByEmail(email);

        if (user.isVerified()) {
            throw new BaseException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }

        user.setVerified(true);

        // The Cached Entity is detached from the EntityManager
        // so we need to save it again
        userService.saveUser(user);

        return createAuthResponse(user);
    }

    @Transactional
    @CacheEvict(value = "user_email", key = "#request.email")
    public void resetPassword(String token, ResetPasswordRequest request) {
        final String email = jwtTokenProvider.getEmailByToken(token);
        final User user = userService.findByEmail(email);

        final String encodedPassword = passwordEncoder.encode(request.password());
        user.changePassword(encodedPassword);
    }

    private AuthResponse createAuthResponse(User user) {
        final UserInfo userInfo = userMapper.toUserInfo(user);
        final String accessToken = jwtTokenProvider.createAccessToken(user);

        return new AuthResponse(userInfo, new TokenResponse(accessToken, AuthConstants.TOKEN_PREFIX,
                AuthConstants.TOKEN_EXPIRES_IN_SECONDS));
    }

    @CacheEvict(value = { "user_email", "profile" }, key = "#user.email")
    public void deleteAccount(User user, DeleteAccountRequest request) {
        validatePassword(request.password(), user.getPassword());

        user.delete();

        userService.saveUser(user);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
