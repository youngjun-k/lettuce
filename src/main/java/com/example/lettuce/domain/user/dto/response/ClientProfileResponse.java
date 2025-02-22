package com.example.lettuce.domain.user.dto.response;

import java.time.LocalDate;

import com.example.lettuce.domain.user.dto.response.ProfileResponse.ProfileSpecificResponse;

public record ClientProfileResponse(
        LocalDate birthday,
        String gender) implements ProfileSpecificResponse {
}
