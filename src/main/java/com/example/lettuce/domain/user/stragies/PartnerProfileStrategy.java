package com.example.lettuce.domain.user.stragies;

import com.example.lettuce.domain.user.dao.PartnerProfile;
import com.example.lettuce.domain.user.dao.Profile;
import com.example.lettuce.domain.user.dto.request.UpdatePartnerProfileRequest;
import com.example.lettuce.domain.user.dto.request.UpdateUserProfileRequest;

public class PartnerProfileStrategy implements ProfileStrategy {

    @Override
    public void validateRequest(UpdateUserProfileRequest<?> request) {
        if (!(request instanceof UpdatePartnerProfileRequest)) {
            throw new IllegalArgumentException("Invalid request type for partner profile");
        }
    }

    @Override
    public void updateProfile(Profile profile, UpdateUserProfileRequest<?> request) {

        validateRequest(request);

        UpdatePartnerProfileRequest partnerRequest = (UpdatePartnerProfileRequest) request;
        PartnerProfile partnerProfile = (PartnerProfile) profile;
        partnerProfile.updateProfile(partnerRequest);
    }
}
