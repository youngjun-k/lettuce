package com.example.lettuce.domain.user.command.dto;

import com.example.lettuce.global.framework.cqrs.Command;

import lombok.Getter;

@Getter
public class LoginCommand implements Command<String> {
    private final String email;
    private final String password;

    public LoginCommand(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String getAggregateId() {
        return email;
    }
}
