package com.example.lettuce.domain.auth.dto.response;

public record TokenResponse(
        String accessToken,
        String tokenType,
        Integer expiresIn) {
}
