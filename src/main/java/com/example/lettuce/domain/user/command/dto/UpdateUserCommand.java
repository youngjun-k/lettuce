package com.example.lettuce.domain.user.command.dto;

import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.cqrs.Command;
import com.example.lettuce.global.framework.security.annotation.ValidNickname;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class UpdateUserCommand implements Command<String> {

    @NotNull
    private User user;

    @ValidNickname
    private String nickname;

    private MultipartFile profileImage;

    @Override
    public String getAggregateId() {
        return nickname;
    }

    public UpdateUserCommand(String nickname, MultipartFile profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
