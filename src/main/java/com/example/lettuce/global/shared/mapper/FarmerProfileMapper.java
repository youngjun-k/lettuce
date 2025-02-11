package com.example.lettuce.global.shared.mapper;

import org.mapstruct.Mapper;

import com.example.lettuce.domain.auth.dto.request.CreateFarmerRequest;
import com.example.lettuce.domain.user.dto.ProfileInfo;
import com.example.lettuce.domain.user.dto.response.FarmerProfileResponse;
import com.example.lettuce.domain.user.entity.FarmerProfile;
import com.example.lettuce.domain.user.entity.User;

@Mapper(componentModel = "spring")
public interface FarmerProfileMapper extends ProfileMapper<CreateFarmerRequest, FarmerProfile> {

    default ProfileInfo toProfileInfo(User user, FarmerProfile farmerProfile) {
        return BaseProfileMapper.toProfileInfo(user, farmerProfile);
    }

    FarmerProfileResponse toProfileSpecificResponse(FarmerProfile farmerProfile);
}
