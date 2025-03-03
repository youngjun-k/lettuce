package com.example.lettuce.domain.user.command.handler;

import org.springframework.stereotype.Component;

import com.example.lettuce.domain.user.aggregate.Profile;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.command.dto.UpdateUserCommand;
import com.example.lettuce.domain.user.mapper.ProfileMapperFactory;
import com.example.lettuce.domain.user.repository.UserRepository;
import com.example.lettuce.domain.user.stragies.ProfileStrategy;
import com.example.lettuce.global.framework.cqrs.CommandHandler;
import com.example.lettuce.global.shared.s3.S3Service;
import com.example.lettuce.global.shared.s3.UploadImageInfo;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommand, Void> {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    @CacheEvict(value = "profile", key = "#user.email")
    public Void handle(UpdateUserCommand command) {

        User user = command.getUser();

        Profile profile = user.getProfile();

        MultipartFile profileImage = command.getProfileImage();

        ProfileStrategy strategy = ProfileMapperFactory.getProfileStrategy(user.getRole());

        strategy.updateProfile(profile, command);

        if (profileImage != null) {
            UploadImageInfo uploadImageInfo = s3Service.uploadMemberProfileImage(profileImage);
            profile.updateProfileImage(uploadImageInfo.imageUrl());
        }
        userRepository.save(user);

        return null;
    }

}
