package com.example.lettuce.domain.user.command.dto;

import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.cqrs.Command;
import lombok.Getter;

@Getter
public class DeleteAccountCommand implements Command<String> {

    private User user;
    private String reason;

    public DeleteAccountCommand(User user, String reason) {
        this.user = user;
        this.reason = reason;
    }

    @Override
    public String getAggregateId() {
        return user.getEmail();
    }
}
