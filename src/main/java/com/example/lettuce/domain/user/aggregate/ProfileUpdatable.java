package com.example.lettuce.domain.user.aggregate;

public interface ProfileUpdatable<T> {
    void updateProfile(T profileRequest);
}