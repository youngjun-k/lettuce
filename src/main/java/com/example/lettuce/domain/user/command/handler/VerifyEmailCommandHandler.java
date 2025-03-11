package com.example.lettuce.domain.user.command.handler;

import org.springframework.stereotype.Component;

import com.example.lettuce.api.user.dto.UserInfo;
import com.example.lettuce.api.user.dto.response.AuthResponse;
import com.example.lettuce.api.user.dto.response.TokenResponse;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.command.dto.VerifyEmailCommand;
import com.example.lettuce.domain.user.mapper.UserMapper;
import com.example.lettuce.domain.user.query.UserQueryService;
import com.example.lettuce.domain.user.repository.UserRepository;
import com.example.lettuce.global.framework.cqrs.CommandHandler;
import com.example.lettuce.global.framework.security.provider.JwtTokenProvider;
import com.example.lettuce.global.shared.constant.AuthConstants;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VerifyEmailCommandHandler implements CommandHandler<VerifyEmailCommand, AuthResponse> {

    private final UserQueryService userQueryService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public AuthResponse handle(VerifyEmailCommand command) {
        String email = jwtTokenProvider.getEmailByToken(command.token());

        User user = userQueryService.findByEmail(email);

        user.verifyEmail();

        userRepository.save(user);

        return createAuthResponse(user);
    }

    private AuthResponse createAuthResponse(User user) {
        final UserInfo userInfo = userMapper.toUserInfo(user);
        final String accessToken = jwtTokenProvider.createAccessToken(user);

        return new AuthResponse(userInfo, new TokenResponse(accessToken, AuthConstants.TOKEN_PREFIX,
                AuthConstants.TOKEN_EXPIRES_IN_SECONDS));
    }
}
