package com.example.lettuce.domain.user.dto.request;


import com.example.lettuce.domain.user.dao.PartnerProfile;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdatePartnerProfileRequest extends UpdateUserProfileRequest<PartnerProfile> {

    @NotBlank(message = "파트너 설명을 입력해주세요.")
    private String partnerDescription;

    @Nullable
    private String partnerWebsite;

    @NotBlank(message = "파트너 전화번호를 입력해주세요.")
    private String partnerPhone;

    @NotBlank(message = "파트너 주소를 입력해주세요.")
    private String partnerAddress;
}


