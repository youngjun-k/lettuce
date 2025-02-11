package com.example.lettuce.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateFarmerRequest extends CreateUserRequest {

    @NotBlank(message = "농가 면허번호를 입력해주세요.")
    private String farmerLicenseNumber;

    @NotBlank(message = "농장 이름을 입력해주세요.")
    @Size(min = 1, max = 45, message = "농장 이름은 1자 이상 45자 이하여야 합니다.")
    private String farmName;

    @NotBlank(message = "농가 설명을 입력해주세요.")
    @Size(min = 10, max = 255, message = "농가 설명은 10자 이상 255자 이하여야 합니다.")
    private String farmerDescription;
}
