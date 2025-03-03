package com.example.lettuce.api.user.dto.response;

public record TokenResponse(
        String accessToken,
        String tokenType,
        Integer expiresIn) {
}
