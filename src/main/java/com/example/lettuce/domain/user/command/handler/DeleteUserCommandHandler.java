package com.example.lettuce.domain.user.command.handler;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.command.dto.DeleteAccountCommand;
import com.example.lettuce.global.framework.cqrs.CommandHandler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteUserCommandHandler implements CommandHandler<DeleteAccountCommand, Void> {

    @Override
    @Transactional
    @CacheEvict(value = { "user_email", "profile" }, key = "#command.user.email")
    public Void handle(DeleteAccountCommand command) {
        User user = command.getUser();
        user.delete();

        return null;
    }
}
