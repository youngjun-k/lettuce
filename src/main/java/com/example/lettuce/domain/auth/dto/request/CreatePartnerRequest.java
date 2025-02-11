package com.example.lettuce.domain.auth.dto.request;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotBlank;

public class CreatePartnerRequest extends CreateUserRequest {

    @NotBlank(message = "파트너 유형을 입력해주세요.")
    private String partnerType;

    @NotBlank(message = "파트너 사업자 등록번호를 입력해주세요.")
    private String partnerRegistrationNumber;

    @NotBlank(message = "파트너 설명을 입력해주세요.")
    private String partnerDescription;

    @Nullable
    private String partnerWebsite;

    @NotBlank(message = "파트너 전화번호를 입력해주세요.")
    private String partnerPhone;

    @NotBlank(message = "파트너 주소를 입력해주세요.")
    private String partnerAddress;
}
