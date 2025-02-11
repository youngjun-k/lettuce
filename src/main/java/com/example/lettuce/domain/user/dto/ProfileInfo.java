package com.example.lettuce.domain.user.dto;

public record ProfileInfo(
        Long userId,
        String email,
        String role,
        String nickname,
        String profileImageUrl) {
}
