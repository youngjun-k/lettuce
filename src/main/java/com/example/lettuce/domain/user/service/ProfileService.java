package com.example.lettuce.domain.user.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lettuce.domain.user.dto.ProfileInfo;
import com.example.lettuce.domain.user.dto.request.UpdateClientProfileRequest;
import com.example.lettuce.domain.user.dto.request.UpdateFarmerProfileRequest;
import com.example.lettuce.domain.user.dto.request.UpdatePartnerProfileRequest;
import com.example.lettuce.domain.user.dto.response.ProfileResponse;
import com.example.lettuce.domain.user.dto.response.ProfileResponse.ProfileSpecificResponse;
import com.example.lettuce.domain.user.entity.ClientProfile;
import com.example.lettuce.domain.user.entity.FarmerProfile;
import com.example.lettuce.domain.user.entity.PartnerProfile;
import com.example.lettuce.domain.user.entity.Profile;
import com.example.lettuce.domain.user.entity.User;
import com.example.lettuce.global.shared.mapper.ClientProfileMapper;
import com.example.lettuce.global.shared.mapper.FarmerProfileMapper;
import com.example.lettuce.global.shared.mapper.PartnerProfileMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final ClientProfileMapper clientProfileMapper;
    private final PartnerProfileMapper partnerProfileMapper;
    private final FarmerProfileMapper farmerProfileMapper;

    public ProfileResponse getProfile(User user) {

        final Profile profile = user.getProfile();

        final ProfileInfo profilInfo = buildProfileInfo(user, profile);
        final ProfileSpecificResponse profileSpecificResponse = buildProfileSpecificResponse(profile);

        return new ProfileResponse(profilInfo, profileSpecificResponse);
    }

    private ProfileInfo buildProfileInfo(User user, Profile profile) {
        return switch (profile) {
            case ClientProfile clientProfile -> clientProfileMapper.toProfileInfo(user, clientProfile);
            case PartnerProfile partnerProfile -> partnerProfileMapper.toProfileInfo(user, partnerProfile);
            case FarmerProfile farmerProfile -> farmerProfileMapper.toProfileInfo(user, farmerProfile);
            default -> throw new IllegalArgumentException("Invalid profile type: " + profile);
        };
    }

    private ProfileSpecificResponse buildProfileSpecificResponse(Profile profile) {
        return switch (profile) {
            case ClientProfile clientProfile -> clientProfileMapper.toProfileSpecificResponse(clientProfile);
            case PartnerProfile partnerProfile -> partnerProfileMapper.toProfileSpecificResponse(partnerProfile);
            case FarmerProfile farmerProfile -> farmerProfileMapper.toProfileSpecificResponse(farmerProfile);
            default -> throw new IllegalArgumentException("Invalid profile type: " + profile);
        };
    }

    @CacheEvict(value = "client_profile", key = "#p0.id")
    public void updateClientProfile(User user, UpdateClientProfileRequest profileRequest) {
        final ClientProfile clientProfile = (ClientProfile) user.getProfile();
        clientProfile.updateProfile(profileRequest);
    }

    @CacheEvict(value = "partner_profile", key = "#p0.id")
    public void updatePartnerProfile(User user, UpdatePartnerProfileRequest profileRequest) {
        final PartnerProfile partnerProfile = (PartnerProfile) user.getProfile();
        partnerProfile.updateProfile(profileRequest);
    }

    @CacheEvict(value = "farmer_profile", key = "#p0.id")
    public void updateFarmerProfile(User user, UpdateFarmerProfileRequest profileRequest) {
        final FarmerProfile farmerProfile = (FarmerProfile) user.getProfile();
        farmerProfile.updateProfile(profileRequest);
    }
}
