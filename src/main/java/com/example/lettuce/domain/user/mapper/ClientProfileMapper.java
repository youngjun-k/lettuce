package com.example.lettuce.domain.user.mapper;

import org.mapstruct.Mapper;
import com.example.lettuce.domain.user.command.dto.CreateClientCommand;
import com.example.lettuce.api.user.dto.response.ClientProfileResponse;
import com.example.lettuce.domain.user.aggregate.ClientProfile;

@Mapper(componentModel = "spring")
public interface ClientProfileMapper extends ProfileMapper<CreateClientCommand, ClientProfile, ClientProfileResponse> {

}