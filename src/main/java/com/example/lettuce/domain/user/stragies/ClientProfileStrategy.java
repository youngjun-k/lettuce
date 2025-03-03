package com.example.lettuce.domain.user.stragies;

import com.example.lettuce.domain.user.aggregate.ClientProfile;
import com.example.lettuce.domain.user.aggregate.Profile;
import com.example.lettuce.domain.user.command.dto.UpdateClientProfileCommand;
import com.example.lettuce.domain.user.command.dto.UpdateUserCommand;

public class ClientProfileStrategy implements ProfileStrategy {
    @Override
    public void validateRequest(UpdateUserCommand request) {
        if (!(request instanceof UpdateClientProfileCommand)) {
            throw new IllegalArgumentException("Invalid request type for client profile");
        }
    }

    @Override
    public void updateProfile(Profile profile, UpdateUserCommand request) {

        validateRequest(request);

        UpdateClientProfileCommand clientRequest = (UpdateClientProfileCommand) request;

        ClientProfile clientProfile = (ClientProfile) profile;
        clientProfile.updateProfile(clientRequest);
    }
}
