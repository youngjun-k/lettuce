package com.example.lettuce.domain.carbonfootprint.command.dto;

import org.springframework.web.multipart.MultipartFile;

import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.cqrs.Command;

public record CalculateFootprintByImageCommand(MultipartFile image, User user) implements Command<String> {

    @Override
    public String getAggregateId() {
        return user.getId().toString();
    }
}
