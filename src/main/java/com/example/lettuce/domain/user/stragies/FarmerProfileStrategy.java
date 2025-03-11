package com.example.lettuce.domain.user.stragies;

import com.example.lettuce.domain.user.aggregate.FarmerProfile;
import com.example.lettuce.domain.user.aggregate.Profile;
import com.example.lettuce.domain.user.command.dto.UpdateFarmerProfileCommand;
import com.example.lettuce.domain.user.command.dto.UpdateUserCommand;

public class FarmerProfileStrategy implements ProfileStrategy {

    @Override
    public void validateRequest(UpdateUserCommand request) {
        if (!(request instanceof UpdateFarmerProfileCommand)) {
            throw new IllegalArgumentException("Invalid request type for farmer profile");
        }
    }

    @Override
    public void updateProfile(Profile profile, UpdateUserCommand request) {

        validateRequest(request);

        UpdateFarmerProfileCommand farmerRequest = (UpdateFarmerProfileCommand) request;
        FarmerProfile farmerProfile = (FarmerProfile) profile;
        farmerProfile.updateProfile(farmerRequest);
    }

}
