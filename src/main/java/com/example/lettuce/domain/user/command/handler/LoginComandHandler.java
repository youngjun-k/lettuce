package com.example.lettuce.domain.user.command.handler;

import org.springframework.stereotype.Component;

import com.example.lettuce.api.user.dto.UserInfo;
import com.example.lettuce.api.user.dto.response.AuthResponse;
import com.example.lettuce.api.user.dto.response.TokenResponse;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.command.dto.LoginCommand;
import com.example.lettuce.domain.user.mapper.UserMapper;
import com.example.lettuce.domain.user.query.UserQueryService;
import com.example.lettuce.global.framework.cqrs.CommandHandler;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;
import com.example.lettuce.global.framework.security.provider.JwtTokenProvider;
import com.example.lettuce.global.shared.constant.AuthConstants;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginComandHandler implements CommandHandler<LoginCommand, AuthResponse> {

    private final UserQueryService userQueryService;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponse handle(LoginCommand command) {
        final User user = userQueryService.findByEmail(command.getEmail());

        if (!user.isVerified()) {
            throw new BaseException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        if (!user.getPassword().isMatch(command.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD);
        }

        return createAuthResponse(user);
    }

    private AuthResponse createAuthResponse(User user) {
        final UserInfo userInfo = userMapper.toUserInfo(user);
        final String accessToken = jwtTokenProvider.createAccessToken(user);

        return new AuthResponse(userInfo, new TokenResponse(accessToken, AuthConstants.TOKEN_PREFIX,
                AuthConstants.TOKEN_EXPIRES_IN_SECONDS));
    }
}
