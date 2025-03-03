package com.example.lettuce.domain.user.command.handler;

import org.springframework.stereotype.Component;

import com.example.lettuce.domain.user.command.dto.ResetPasswordCommand;
import com.example.lettuce.domain.user.query.UserQueryService;
import com.example.lettuce.global.framework.cqrs.CommandHandler;
import com.example.lettuce.global.framework.security.provider.JwtTokenProvider;
import com.example.lettuce.domain.user.aggregate.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ResetPasswordCommandHandler implements CommandHandler<ResetPasswordCommand, Void> {

    private final UserQueryService userQueryService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Void handle(ResetPasswordCommand command) {
        final String email = jwtTokenProvider.getEmailByToken(command.getToken());
        final User user = userQueryService.findByEmail(email);
        user.changePassword(command.getNewPassword());
        return null;
    }
}
