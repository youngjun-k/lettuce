package com.example.lettuce.domain.user.stragies;

import com.example.lettuce.domain.user.aggregate.PartnerProfile;
import com.example.lettuce.domain.user.aggregate.Profile;
import com.example.lettuce.domain.user.command.dto.UpdatePartnerProfileCommand;
import com.example.lettuce.domain.user.command.dto.UpdateUserCommand;

public class PartnerProfileStrategy implements ProfileStrategy {

    @Override
    public void validateRequest(UpdateUserCommand request) {
        if (!(request instanceof UpdatePartnerProfileCommand)) {
            throw new IllegalArgumentException("Invalid request type for partner profile");
        }
    }

    @Override
    public void updateProfile(Profile profile, UpdateUserCommand request) {

        validateRequest(request);

        UpdatePartnerProfileCommand partnerRequest = (UpdatePartnerProfileCommand) request;
        PartnerProfile partnerProfile = (PartnerProfile) profile;
        partnerProfile.updateProfile(partnerRequest);
    }
}
