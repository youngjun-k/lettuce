package com.example.lettuce.domain.user.query;

import com.example.lettuce.api.user.dto.response.ProfileResponse;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.cqrs.Query;

public record UserProfileQuery(User user) implements Query<ProfileResponse> {
}