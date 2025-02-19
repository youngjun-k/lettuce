package com.example.lettuce.domain.user.mapper;

import org.mapstruct.Mapper;
import com.example.lettuce.domain.auth.dto.request.CreateClientRequest;
import com.example.lettuce.domain.user.dto.response.ClientProfileResponse;
import com.example.lettuce.domain.user.entity.ClientProfile;

@Mapper(componentModel = "spring")
public interface ClientProfileMapper extends ProfileMapper<CreateClientRequest, ClientProfile, ClientProfileResponse> {

}