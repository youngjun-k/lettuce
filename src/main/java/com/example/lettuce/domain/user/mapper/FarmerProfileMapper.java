package com.example.lettuce.domain.user.mapper;

import org.mapstruct.Mapper;

import com.example.lettuce.domain.auth.dto.request.CreateFarmerRequest;
import com.example.lettuce.domain.user.dao.FarmerProfile;
import com.example.lettuce.domain.user.dto.response.FarmerProfileResponse;


@Mapper(componentModel = "spring")
public interface FarmerProfileMapper extends ProfileMapper<CreateFarmerRequest, FarmerProfile, FarmerProfileResponse> {

}
