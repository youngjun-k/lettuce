package com.example.lettuce.api.user.dto.request;

import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.security.annotation.ValidNickname;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class UpdateUserRequest {

    @NotNull
    private User user;

    @ValidNickname
    private String nickname;

}
