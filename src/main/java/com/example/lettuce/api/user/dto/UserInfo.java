package com.example.lettuce.api.user.dto;

public record UserInfo(
        Long userId,
        String email,
        String role
        ) {
}
