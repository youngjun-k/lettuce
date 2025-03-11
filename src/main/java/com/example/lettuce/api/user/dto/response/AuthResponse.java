package com.example.lettuce.api.user.dto.response;

import com.example.lettuce.api.user.dto.UserInfo;

public record AuthResponse(
        UserInfo userInfo,
        TokenResponse token) {
}
