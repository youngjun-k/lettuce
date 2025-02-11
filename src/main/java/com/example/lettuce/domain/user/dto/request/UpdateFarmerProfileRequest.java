package com.example.lettuce.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateFarmerProfileRequest(
    

    @NotBlank(message = "농가 면허번호를 입력해주세요.")
    String farmerLicenseNumber,

    @NotBlank(message = "농가 설명을 입력해주세요.")
    String farmerDescription) {
}
