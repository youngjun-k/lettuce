package com.example.lettuce.global.shared.mapper;

import com.example.lettuce.domain.user.dto.ProfileInfo;
import com.example.lettuce.domain.user.dto.response.ProfileResponse.ProfileSpecificResponse;
import com.example.lettuce.domain.user.entity.Profile;
import com.example.lettuce.domain.user.entity.User;

public interface ProfileMapper<T, P extends Profile> {
    P toProfile(T request, User user);

    ProfileInfo toProfileInfo(User user, P profile);

    ProfileSpecificResponse toProfileSpecificResponse(P profile);
}