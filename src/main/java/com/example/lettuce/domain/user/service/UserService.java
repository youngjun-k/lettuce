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
@CacheConfig(cacheNames = { "user_email", "user_id" })
public class UserService {
    private final UserRepository userRepository;

    /**
     * 
     * @param email
     * @return User
     * @throws BaseException if the user is not found
     */
    @Cacheable(value = "user_email", key = "#p0")    
    public User findByEmail(String email) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email);
        if (user == null) {
            throw new BaseException(ErrorCode.NOT_FOUND_USER);
        }
        return user;
    }

    /**
     * 
     * @param id
     * @return User
     * @throws BaseException if the user is not found
     */
    @Cacheable(value = "user_id", key = "#p0")
    public User findByUserId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
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
     * @throws BaseException if the email is not registered
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
     * @param user
     */
    @CacheEvict(value = { "user_email", "user_id" }, key = "#p0.email")
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
