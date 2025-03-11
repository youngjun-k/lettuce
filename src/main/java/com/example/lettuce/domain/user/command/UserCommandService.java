package com.example.lettuce.domain.user.command;

import org.springframework.stereotype.Service;

import com.example.lettuce.api.user.dto.response.AuthResponse;
import com.example.lettuce.domain.user.command.dto.CreateUserCommand;
import com.example.lettuce.domain.user.command.dto.DeleteAccountCommand;
import com.example.lettuce.domain.user.command.dto.LoginCommand;
import com.example.lettuce.domain.user.command.dto.ResetPasswordCommand;
import com.example.lettuce.domain.user.command.dto.UpdateUserCommand;
import com.example.lettuce.domain.user.command.dto.VerifyEmailCommand;
import com.example.lettuce.global.framework.cqrs.CommandBus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final CommandBus commandBus;

    public AuthResponse login(LoginCommand command) {
        return commandBus.dispatch(command);
    }

    public <T extends CreateUserCommand> void registerUser(T command) {
        commandBus.dispatch(command);
    }

    public <T extends UpdateUserCommand> void updateProfile(T command) {
        commandBus.dispatch(command);
    }

    public void deleteAccount(DeleteAccountCommand command) {
        commandBus.dispatch(command);
    }

    public AuthResponse verifyEmail(VerifyEmailCommand command) {
        return commandBus.dispatch(command);
    }

    public void resetPassword(ResetPasswordCommand command) {
        commandBus.dispatch(command);
    }
}
