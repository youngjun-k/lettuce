package com.example.lettuce.domain.user.mapper;

import org.mapstruct.Mapper;

import com.example.lettuce.domain.auth.dto.request.CreatePartnerRequest;
import com.example.lettuce.domain.user.dao.PartnerProfile;
import com.example.lettuce.domain.user.dto.response.PartnerProfileResponse;

@Mapper(componentModel = "spring")
public interface PartnerProfileMapper
        extends ProfileMapper<CreatePartnerRequest, PartnerProfile, PartnerProfileResponse> {

}
