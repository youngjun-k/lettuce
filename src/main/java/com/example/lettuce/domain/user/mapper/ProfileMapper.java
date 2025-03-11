package com.example.lettuce.domain.user.mapper;

import com.example.lettuce.api.user.dto.ProfileInfo;
import com.example.lettuce.api.user.dto.response.ProfileResponse.ProfileSpecificResponse;
import com.example.lettuce.domain.user.aggregate.Profile;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.command.dto.CreateUserCommand;

public interface ProfileMapper<T extends CreateUserCommand, P extends Profile, R extends ProfileSpecificResponse> {
    P toProfile(T request, User user);

    ProfileInfo toProfileInfo(User user, P profile);

    R toProfileSpecificResponse(P profile);
}