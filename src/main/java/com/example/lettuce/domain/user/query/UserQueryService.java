package com.example.lettuce.domain.user.query;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.lettuce.api.user.dto.response.ProfileResponse;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.repository.UserRepository;
import com.example.lettuce.global.framework.cqrs.QueryBus;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

import org.springframework.cache.annotation.CacheConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = { "user_email" })
public class UserQueryService {

    private final QueryBus queryBus;
    private final UserRepository userRepository;

    @Cacheable(value = "profile", key = "#query.user.email")
    public ProfileResponse getProfile(UserProfileQuery query) {
        return queryBus.execute(query);
    }

    @Cacheable(value = "user_email", key = "#email")
    public User findByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
    }

    @Cacheable(value = "user_email_with_profiles", key = "#email")
    public User findByEmailWithProfiles(String email) {
        return userRepository.findByEmailAndDeletedAtIsNullWithProfiles(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
    }
}