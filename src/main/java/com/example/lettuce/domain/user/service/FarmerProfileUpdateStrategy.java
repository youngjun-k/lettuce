package com.example.lettuce.domain.user.service;

import com.example.lettuce.domain.user.dto.request.UpdateFarmerProfileRequest;
import com.example.lettuce.domain.user.dto.request.UpdateUserProfileRequest;
import com.example.lettuce.domain.user.entity.FarmerProfile;
import com.example.lettuce.domain.user.entity.Profile;

public class FarmerProfileUpdateStrategy implements ProfileUpdateStrategy {

    @Override
    public void validateRequest(UpdateUserProfileRequest<?> request) {
        if (!(request instanceof UpdateFarmerProfileRequest)) {
            throw new IllegalArgumentException("Invalid request type for farmer profile");
        }
    }

    @Override
    public void updateProfile(Profile profile, UpdateUserProfileRequest<?> request) {
        UpdateFarmerProfileRequest farmerRequest = (UpdateFarmerProfileRequest) request;
        FarmerProfile farmerProfile = (FarmerProfile) profile;
        farmerProfile.updateProfile(farmerRequest);
    }
}
