package com.example.lettuce.domain.auth.service;

import java.util.function.BiFunction;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.lettuce.domain.auth.controller.DeleteAccountRequest;
import com.example.lettuce.domain.auth.dto.request.CreateClientRequest;
import com.example.lettuce.domain.auth.dto.request.CreateFarmerRequest;
import com.example.lettuce.domain.auth.dto.request.CreatePartnerRequest;
import com.example.lettuce.domain.auth.dto.request.CreateUserRequest;
import com.example.lettuce.domain.auth.dto.request.LoginRequest;
import com.example.lettuce.domain.auth.dto.request.ResetPasswordRequest;
import com.example.lettuce.domain.auth.dto.response.AuthResponse;
import com.example.lettuce.domain.auth.dto.response.TokenResponse;
import com.example.lettuce.domain.user.dto.UserInfo;
import com.example.lettuce.domain.user.entity.Profile;
import com.example.lettuce.domain.user.entity.User;
import com.example.lettuce.domain.user.enums.UserRole;
import com.example.lettuce.domain.user.service.UserService;
import com.example.lettuce.global.framework.security.provider.JwtTokenProvider;
import com.example.lettuce.global.shared.constant.AuthConstants;

import com.example.lettuce.global.shared.mapper.ClientProfileMapper;
import com.example.lettuce.global.shared.mapper.FarmerProfileMapper;
import com.example.lettuce.global.shared.mapper.PartnerProfileMapper;
import com.example.lettuce.global.shared.mapper.UserMapper;
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
    private final ClientProfileMapper clientProfileMapper;
    private final PartnerProfileMapper partnerProfileMapper;
    private final FarmerProfileMapper farmerProfileMapper;
    private final ApplicationEventPublisher eventPublisher;

    public AuthResponse login(LoginRequest request) {

        final User user = userService.findByEmail(request.email());

        if (!user.isVerified()) {
            throw new AuthenticationException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        validatePassword(request.password(), user.getPassword());

        return createAuthResponse(user);
    }

    public void clientRegister(CreateClientRequest request) {
        registerUser(request, UserRole.CLIENT, clientProfileMapper::toProfile);
    }

    public void partnerRegister(CreatePartnerRequest request) {
        registerUser(request, UserRole.BUSINESS_PARTNER, partnerProfileMapper::toProfile);
    }

    public void farmerRegister(CreateFarmerRequest request) {
        registerUser(request, UserRole.FARMER, farmerProfileMapper::toProfile);
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
    @CacheEvict(value = "user_email", key = "#p0.email")
    private <T extends CreateUserRequest> void registerUser(T request, UserRole role,
            BiFunction<T, User, Profile> profileMapper) {
        final String email = request.getEmail();
        final String rawPassword = request.getPassword();

        userService.isNotRegisteredEmail(email);

        final String encodedPassword = passwordEncoder.encode(rawPassword);
        final User user = userMapper.toUser(request, encodedPassword, role);

        final Profile profile = profileMapper.apply(request, user);
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
