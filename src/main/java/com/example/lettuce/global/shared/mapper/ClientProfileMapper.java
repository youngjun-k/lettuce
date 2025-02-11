package com.example.lettuce.global.shared.mapper;

import org.mapstruct.Mapper;
import com.example.lettuce.domain.auth.dto.request.CreateClientRequest;
import com.example.lettuce.domain.user.dto.ProfileInfo;
import com.example.lettuce.domain.user.dto.response.ClientProfileResponse;
import com.example.lettuce.domain.user.entity.ClientProfile;
import com.example.lettuce.domain.user.entity.User;

@Mapper(componentModel = "spring")
public interface ClientProfileMapper extends ProfileMapper<CreateClientRequest, ClientProfile> {

    default ProfileInfo toProfileInfo(User user, ClientProfile clientProfile) {
        return BaseProfileMapper.toProfileInfo(user, clientProfile);
    }

    ClientProfileResponse toProfileSpecificResponse(ClientProfile clientProfile);

}