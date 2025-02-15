package com.example.lettuce.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateFarmerProfileRequest extends UpdateUserProfileRequest {

    @NotBlank(message = "농가 면허번호를 입력해주세요.")
    private String farmerLicenseNumber;

    @NotBlank(message = "농가 설명을 입력해주세요.")
    private String farmerDescription;
}
