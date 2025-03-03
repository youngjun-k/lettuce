package com.example.lettuce.domain.user.command.dto;

import com.example.lettuce.global.framework.cqrs.Command;

import lombok.Getter;

@Getter
public class ResetPasswordCommand implements Command<String> {
    private String token;
    private String newPassword;

    public ResetPasswordCommand(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    @Override
    public String getAggregateId() {
        return token;
    }
}
