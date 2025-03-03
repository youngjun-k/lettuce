package com.example.lettuce.api.user.dto.response;

import java.time.LocalDate;

import com.example.lettuce.api.user.dto.response.ProfileResponse.ProfileSpecificResponse;

public record ClientProfileResponse(
        LocalDate birthday,
        String gender) implements ProfileSpecificResponse {
}
