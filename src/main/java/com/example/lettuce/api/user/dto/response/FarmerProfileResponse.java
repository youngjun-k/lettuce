package com.example.lettuce.api.user.dto.response;

import com.example.lettuce.api.user.dto.response.ProfileResponse.ProfileSpecificResponse;

public record FarmerProfileResponse(
        String farmerLicenseNumber,
        String farmerDescription) implements ProfileSpecificResponse {
}
