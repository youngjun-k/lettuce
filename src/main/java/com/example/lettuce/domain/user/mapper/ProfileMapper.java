package com.example.lettuce.domain.user.mapper;

import com.example.lettuce.domain.auth.dto.request.CreateUserRequest;
import com.example.lettuce.domain.user.dao.Profile;
import com.example.lettuce.domain.user.dao.User;
import com.example.lettuce.domain.user.dto.ProfileInfo;
import com.example.lettuce.domain.user.dto.response.ProfileResponse.ProfileSpecificResponse;

public interface ProfileMapper<T extends CreateUserRequest, P extends Profile, R extends ProfileSpecificResponse> {
    P toProfile(T request, User user);

    ProfileInfo toProfileInfo(User user, P profile);

    R toProfileSpecificResponse(P profile);
}