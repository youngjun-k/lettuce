package com.example.lettuce.domain.user.service;

import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
import com.example.lettuce.domain.user.repository.UserRepository;
import com.example.lettuce.global.shared.mapper.ClientProfileMapper;
import com.example.lettuce.global.shared.mapper.FarmerProfileMapper;
import com.example.lettuce.global.shared.mapper.PartnerProfileMapper;
import com.example.lettuce.global.shared.mapper.ProfileMapper;
import com.example.lettuce.global.shared.s3.S3Service;
import com.example.lettuce.global.shared.s3.UploadImageInfo;

@Service
@Transactional(readOnly = true)
public class ProfileService {

    private final Map<Class<? extends Profile>, ProfileMapper<?, ?>> mappers;
    private final S3Service s3Service;
    private final UserRepository userRepository;

    public ProfileService(ClientProfileMapper clientProfileMapper, PartnerProfileMapper partnerProfileMapper,
            FarmerProfileMapper farmerProfileMapper, S3Service s3Service,
            UserRepository userRepository) {

        this.mappers = Map.of(
                ClientProfile.class, clientProfileMapper,
                PartnerProfile.class, partnerProfileMapper,
                FarmerProfile.class, farmerProfileMapper);

        this.s3Service = s3Service;
        this.userRepository = userRepository;
    }

    @Cacheable(value = "profile", key = "#user.id")
    public ProfileResponse getProfile(User user) {
        Profile profile = user.getProfile();

        @SuppressWarnings("unchecked")
        ProfileMapper<Object, Profile> mapper = (ProfileMapper<Object, Profile>) this.mappers.get(profile.getClass());

        return new ProfileResponse(
                mapper.toProfileInfo(user, profile),
                mapper.toProfileSpecificResponse(profile));
    }

    @Transactional
    @CacheEvict(value = "profile", key = "#user.id")
    public void updateClientProfile(User user, UpdateClientProfileRequest profileRequest, MultipartFile profileImage) {
        updateProfile(user, profileRequest, profileImage);
    }

    @Transactional
    @CacheEvict(value = "profile", key = "#user.id")
    public void updatePartnerProfile(User user, UpdatePartnerProfileRequest profileRequest,
            MultipartFile profileImage) {
        updateProfile(user, profileRequest, profileImage);
    }

    @Transactional
    @CacheEvict(value = "profile", key = "#user.id")
    public void updateFarmerProfile(User user, UpdateFarmerProfileRequest profileRequest, MultipartFile profileImage) {
        updateProfile(user, profileRequest, profileImage);
    }

    private <T extends Profile> void updateProfile(User user, Object profileRequest, MultipartFile profileImage) {
        final Profile profile = user.getProfile();
        if (profile instanceof ProfileUpdatable<?>) {
            ((ProfileUpdatable<Object>) profile).updateProfile(profileRequest);
        }
        if (profileImage != null) {
            UploadImageInfo uploadImageInfo = s3Service.uploadMemberProfileImage(profileImage);
            profile.updateProfileImage(uploadImageInfo.imageUrl());
        }
        userRepository.save(user);
    }
}
