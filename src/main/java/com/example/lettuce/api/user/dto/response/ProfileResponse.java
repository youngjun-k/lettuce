package com.example.lettuce.api.user.dto.response;

import com.example.lettuce.api.user.dto.ProfileInfo;

public record ProfileResponse(
        ProfileInfo info,
        ProfileSpecificResponse profile) {
    public interface ProfileSpecificResponse {
    }
}