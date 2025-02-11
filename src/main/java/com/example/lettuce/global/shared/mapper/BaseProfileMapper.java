package com.example.lettuce.global.shared.mapper;

import com.example.lettuce.domain.user.dto.ProfileInfo;
import com.example.lettuce.domain.user.entity.Profile;
import com.example.lettuce.domain.user.entity.User;

public abstract class BaseProfileMapper {

    protected static ProfileInfo toProfileInfo(User user, Profile profile) {
        return new ProfileInfo(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                profile.getNickname(),
                profile.getProfileImage());
    }
}