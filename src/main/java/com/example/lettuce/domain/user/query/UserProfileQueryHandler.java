package com.example.lettuce.domain.user.query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.lettuce.global.framework.cqrs.QueryHandler;

import lombok.RequiredArgsConstructor;

import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.api.user.dto.response.ProfileResponse;
import com.example.lettuce.domain.user.aggregate.Profile;
import com.example.lettuce.domain.user.mapper.ProfileMapper;
import com.example.lettuce.domain.user.mapper.ProfileMapperFactory;

@Component
@RequiredArgsConstructor
public class UserProfileQueryHandler implements QueryHandler<UserProfileQuery, ProfileResponse> {

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse handle(UserProfileQuery query) {
        User user = query.user();
        Profile profile = user.getProfile();

        @SuppressWarnings("unchecked")
        ProfileMapper<?, Profile, ?> mapper = (ProfileMapper<?, Profile, ?>) ProfileMapperFactory
                .getProfileMapper(user.getRole());

        return new ProfileResponse(
                mapper.toProfileInfo(user, profile),
                mapper.toProfileSpecificResponse(profile));
    }

}
