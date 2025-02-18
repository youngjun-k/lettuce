package com.example.lettuce.domain.user.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.lettuce.domain.user.dto.request.UpdateUserProfileRequest;
import com.example.lettuce.domain.user.dto.response.ProfileResponse;
import com.example.lettuce.domain.user.entity.Profile;
import com.example.lettuce.domain.user.entity.User;
import com.example.lettuce.domain.user.repository.UserRepository;
import com.example.lettuce.global.shared.mapper.ProfileMapper;
import com.example.lettuce.global.shared.s3.S3Service;
import com.example.lettuce.global.shared.s3.UploadImageInfo;

@Service
@Transactional(readOnly = true)
public class ProfileService {

    private final S3Service s3Service;
    private final UserRepository userRepository;

    public ProfileService(S3Service s3Service,
            UserRepository userRepository) {

        this.s3Service = s3Service;
        this.userRepository = userRepository;
    }

    @Cacheable(value = "profile", key = "#user.id")
    public ProfileResponse getProfile(User user) {
        Profile profile = user.getProfile();

        @SuppressWarnings("unchecked")
        ProfileMapper<?, Profile, ?> mapper = (ProfileMapper<?, Profile, ?>) ProfileMapperFactory
                .getProfileMapper(user.getRole());

        return new ProfileResponse(
                mapper.toProfileInfo(user, profile),
                mapper.toProfileSpecificResponse(profile));
    }

    @Transactional
    @CacheEvict(value = "profile", key = "#user.id")
    public <T extends Profile> void updateProfile(User user, UpdateUserProfileRequest<T> profileRequest,
            MultipartFile profileImage) {

        Profile profile = user.getProfile();

        ProfileStrategy strategy = ProfileMapperFactory.getProfileStrategy(user.getRole());

        strategy.validateRequest(profileRequest);
        strategy.updateProfile(profile, profileRequest);

        if (profileImage != null) {
            UploadImageInfo uploadImageInfo = s3Service.uploadMemberProfileImage(profileImage);
            profile.updateProfileImage(uploadImageInfo.imageUrl());
        }
        userRepository.save(user);
    }
}
