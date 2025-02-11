package com.example.lettuce.domain.auth.dto.response;

import com.example.lettuce.domain.user.dto.UserInfo;

public record AuthResponse(
        UserInfo userInfo,
        TokenResponse token) {
}
