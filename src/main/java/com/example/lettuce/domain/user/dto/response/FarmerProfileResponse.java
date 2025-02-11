package com.example.lettuce.domain.user.dto.response;

import com.example.lettuce.domain.user.dto.response.ProfileResponse.ProfileSpecificResponse;

public record FarmerProfileResponse(
        String farmerLicenseNumber,
        String farmerDescription) implements ProfileSpecificResponse {
}
