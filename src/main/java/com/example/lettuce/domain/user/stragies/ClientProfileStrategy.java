package com.example.lettuce.domain.user.stragies;

import com.example.lettuce.domain.user.dao.ClientProfile;
import com.example.lettuce.domain.user.dao.Profile;
import com.example.lettuce.domain.user.dto.request.UpdateClientProfileRequest;
import com.example.lettuce.domain.user.dto.request.UpdateUserProfileRequest;

public class ClientProfileStrategy implements ProfileStrategy {
    @Override
    public void validateRequest(UpdateUserProfileRequest<?> request) {
        if (!(request instanceof UpdateClientProfileRequest)) {
            throw new IllegalArgumentException("Invalid request type for client profile");
        }
    }

    @Override
    public void updateProfile(Profile profile, UpdateUserProfileRequest<?> request) {

        validateRequest(request);

        UpdateClientProfileRequest clientRequest = (UpdateClientProfileRequest) request;

        ClientProfile clientProfile = (ClientProfile) profile;
        clientProfile.updateProfile(clientRequest);
    }
}
