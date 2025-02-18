package com.example.lettuce.domain.user.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.lettuce.domain.user.entity.User;
import com.example.lettuce.domain.user.repository.UserRepository;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = { "user_email" })
public class UserService {
    private final UserRepository userRepository;

    /**
     * @param email
     * @return User
     * @throws BaseException if the user is not found
     */
    @Cacheable(value = "user_email", key = "#p0")
    public User findByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
    }

    @Cacheable(value = "user_email", key = "#p0")
    public User findByEmailWithProfiles(String email) {
        return userRepository.findByEmailAndDeletedAtIsNullWithProfiles(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
    }

    /**
     * @param user Need to evict the cache after saving the user
     */
    @CacheEvict(value = "user_email", key = "#user.email")
    public void saveUser(User user) {
        userRepository.save(user);
    }

    /**
     * 
     * @param email
     * @throws BaseException if the email is already registered
     */
    @Cacheable(value = "user_email", key = "#p0")
    public void isNotRegisteredEmail(String email) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new BaseException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    /**
     * 
     * @param email
     * @throws BaseException if the email is not verified
     * @throws BaseException if the email is not verified
     */
    @Cacheable(value = "user_email", key = "#p0")
    public void validateEmail(String email) {
        User user = findByEmail(email);
        if (!user.isVerified()) {
            throw new BaseException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
    }

    /**
     * 
     * @param email
     * @throws BaseException if the email is not verified
     */
    @Cacheable(value = "user_email", key = "#p0")
    public void validateEmailNotVerified(String email) {
        User user = findByEmail(email);
        if (user.isVerified()) {
            throw new BaseException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }
    }

}