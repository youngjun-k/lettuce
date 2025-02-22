package com.example.lettuce.domain.user.stragies;

import com.example.lettuce.domain.user.dao.FarmerProfile;
import com.example.lettuce.domain.user.dao.Profile;
import com.example.lettuce.domain.user.dto.request.UpdateFarmerProfileRequest;
import com.example.lettuce.domain.user.dto.request.UpdateUserProfileRequest;

public class FarmerProfileStrategy implements ProfileStrategy {

    @Override
    public void validateRequest(UpdateUserProfileRequest<?> request) {
        if (!(request instanceof UpdateFarmerProfileRequest)) {
            throw new IllegalArgumentException("Invalid request type for farmer profile");
        }
    }

    @Override
    public void updateProfile(Profile profile, UpdateUserProfileRequest<?> request) {

        validateRequest(request);

        UpdateFarmerProfileRequest farmerRequest = (UpdateFarmerProfileRequest) request;
        FarmerProfile farmerProfile = (FarmerProfile) profile;
        farmerProfile.updateProfile(farmerRequest);
    }

}
