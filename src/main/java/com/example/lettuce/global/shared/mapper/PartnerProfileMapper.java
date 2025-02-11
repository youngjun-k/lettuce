package com.example.lettuce.global.shared.mapper;

import org.mapstruct.Mapper;

import com.example.lettuce.domain.auth.dto.request.CreatePartnerRequest;
import com.example.lettuce.domain.user.dto.ProfileInfo;
import com.example.lettuce.domain.user.dto.response.PartnerProfileResponse;
import com.example.lettuce.domain.user.entity.PartnerProfile;
import com.example.lettuce.domain.user.entity.User;

@Mapper(componentModel = "spring")
public interface PartnerProfileMapper
        extends ProfileMapper<CreatePartnerRequest, PartnerProfile> {

    default ProfileInfo toProfileInfo(User user, PartnerProfile partnerProfile) {
        return BaseProfileMapper.toProfileInfo(user, partnerProfile);
    }

    PartnerProfileResponse toProfileSpecificResponse(PartnerProfile partnerProfile);
}
