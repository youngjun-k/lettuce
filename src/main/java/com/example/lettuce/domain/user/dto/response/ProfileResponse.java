package com.example.lettuce.domain.user.dto.response;

import com.example.lettuce.domain.user.dto.ProfileInfo;

public record ProfileResponse(
        ProfileInfo info,
        ProfileSpecificResponse profile) {
    public interface ProfileSpecificResponse {
    }
}
