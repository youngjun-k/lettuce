package com.example.lettuce.domain.user.mapper;

import org.mapstruct.Mapper;

import com.example.lettuce.domain.user.command.dto.CreatePartnerCommand;
import com.example.lettuce.api.user.dto.response.PartnerProfileResponse;
import com.example.lettuce.domain.user.aggregate.PartnerProfile;

@Mapper(componentModel = "spring")
public interface PartnerProfileMapper
                extends ProfileMapper<CreatePartnerCommand, PartnerProfile, PartnerProfileResponse> {

}
