package com.example.lettuce.api.user.dto.response;

import com.example.lettuce.api.user.dto.response.ProfileResponse.ProfileSpecificResponse;

public record PartnerProfileResponse(
        String partnerType,
        String partnerRegistrationNumber,
        String partnerDescription,
        String partnerWebsite,
        String partnerPhone,
        String partnerAddress) implements ProfileSpecificResponse {
}
