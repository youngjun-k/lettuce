package com.example.lettuce.domain.user.mapper;

import org.mapstruct.Mapper;

import com.example.lettuce.domain.user.command.dto.CreateFarmerCommand;
import com.example.lettuce.api.user.dto.response.FarmerProfileResponse;
import com.example.lettuce.domain.user.aggregate.FarmerProfile;


@Mapper(componentModel = "spring")
public interface FarmerProfileMapper extends ProfileMapper<CreateFarmerCommand, FarmerProfile, FarmerProfileResponse> {

}
