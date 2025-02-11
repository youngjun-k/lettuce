package com.example.lettuce.domain.user.service;

public interface ProfileUpdatable<T> {
    void updateProfile(T profileRequest);
}