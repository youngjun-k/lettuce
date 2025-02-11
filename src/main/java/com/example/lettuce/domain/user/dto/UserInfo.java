package com.example.lettuce.domain.user.dto;

public record UserInfo(
        Long userId,
        String email,
        String role
        ) {
}
