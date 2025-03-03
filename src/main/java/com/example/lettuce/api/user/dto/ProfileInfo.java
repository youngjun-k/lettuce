package com.example.lettuce.api.user.dto;

public record ProfileInfo(
        Long userId,
        String email,
        String role,
        String nickname,
        String profileImageUrl) {
}
