package com.example.lettuce.domain.user.service;

import com.example.lettuce.domain.user.dto.request.UpdateClientProfileRequest;
import com.example.lettuce.domain.user.dto.request.UpdateUserProfileRequest;
import com.example.lettuce.domain.user.entity.ClientProfile;
import com.example.lettuce.domain.user.entity.Profile;

public class ClientProfileUpdateStrategy implements ProfileUpdateStrategy {
    @Override
    public void validateRequest(UpdateUserProfileRequest<?> request) {
        if (!(request instanceof UpdateClientProfileRequest)) {
            throw new IllegalArgumentException("Invalid request type for client profile");
        }
    }

    @Override
    public void updateProfile(Profile profile, UpdateUserProfileRequest<?> request) {
        UpdateClientProfileRequest clientRequest = (UpdateClientProfileRequest) request;
        ClientProfile clientProfile = (ClientProfile) profile;
        clientProfile.updateProfile(clientRequest);
    }
}