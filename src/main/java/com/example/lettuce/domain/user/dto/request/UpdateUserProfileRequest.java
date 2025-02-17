package com.example.lettuce.domain.user.dto.request;

import com.example.lettuce.domain.user.entity.Profile;
import com.example.lettuce.global.framework.security.annotation.ValidNickname;

import lombok.Getter;

@Getter
public abstract class UpdateUserProfileRequest<T extends Profile> {
    @ValidNickname
    private String nickname;    
}

