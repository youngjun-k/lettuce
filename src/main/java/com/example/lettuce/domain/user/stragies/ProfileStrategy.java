package com.example.lettuce.domain.user.stragies;

import com.example.lettuce.domain.user.dao.Profile;
import com.example.lettuce.domain.user.dto.request.UpdateUserProfileRequest;

public interface ProfileStrategy {

    void validateRequest(UpdateUserProfileRequest<?> request);

    void updateProfile(Profile profile, UpdateUserProfileRequest<?> request);
}
