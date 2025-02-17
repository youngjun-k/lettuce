package com.example.lettuce.global.shared.mapper;

import org.mapstruct.Mapper;

import com.example.lettuce.domain.auth.dto.request.CreateFarmerRequest;
import com.example.lettuce.domain.user.dto.response.FarmerProfileResponse;
import com.example.lettuce.domain.user.entity.FarmerProfile;


@Mapper(componentModel = "spring")
public interface FarmerProfileMapper extends ProfileMapper<CreateFarmerRequest, FarmerProfile, FarmerProfileResponse> {

}
