package com.example.lettuce.global.shared.mapper;

import com.example.lettuce.domain.auth.dto.request.CreateUserRequest;
import com.example.lettuce.domain.user.entity.Profile;
import com.example.lettuce.domain.user.entity.User;

public interface ProfileMapper<T extends CreateUserRequest, P extends Profile> {
    P toProfile(T request, User user);
}