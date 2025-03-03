package com.example.lettuce.domain.user.command.dto;

import com.example.lettuce.global.framework.cqrs.Command;

public record VerifyEmailCommand(String token) implements Command<String> {

    @Override
    public String getAggregateId() {
        return token;
    }
}
