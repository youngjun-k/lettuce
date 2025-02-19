package com.example.lettuce.domain.user.mapper;

import org.mapstruct.Mapper;

import com.example.lettuce.domain.auth.dto.request.CreatePartnerRequest;
import com.example.lettuce.domain.user.dto.response.PartnerProfileResponse;
import com.example.lettuce.domain.user.entity.PartnerProfile;

@Mapper(componentModel = "spring")
public interface PartnerProfileMapper
        extends ProfileMapper<CreatePartnerRequest, PartnerProfile, PartnerProfileResponse> {

}
