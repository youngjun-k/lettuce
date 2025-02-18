package com.example.lettuce.domain.user.service;

import com.example.lettuce.domain.user.dto.request.UpdateUserProfileRequest;
import com.example.lettuce.domain.user.entity.Profile;

public interface ProfileStrategy {

    void validateRequest(UpdateUserProfileRequest<?> request);

    void updateProfile(Profile profile, UpdateUserProfileRequest<?> request);
}
