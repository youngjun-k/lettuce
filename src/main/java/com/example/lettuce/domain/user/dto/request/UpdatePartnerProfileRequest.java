package com.example.lettuce.domain.user.dto.request;


import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotBlank;

public record UpdatePartnerProfileRequest(
    @NotBlank(message = "파트너 설명을 입력해주세요.")
    String partnerDescription,

    @Nullable
    String partnerWebsite,

    @NotBlank(message = "파트너 전화번호를 입력해주세요.")
    String partnerPhone,

    @NotBlank(message = "파트너 주소를 입력해주세요.")
    String partnerAddress) {
}


