package com.example.lettuce.api.user.dto.request;

import com.example.lettuce.domain.user.command.dto.CreateFarmerCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateFarmerRequest extends CreateUserRequest {

    @NotBlank(message = "농가 면허번호를 입력해주세요.")
    private String farmerLicenseNumber;

    @NotBlank(message = "농장 이름을 입력해주세요.")
    @Size(min = 1, max = 45, message = "농장 이름은 1자 이상 45자 이하여야 합니다.")
    private String farmName;

    @NotBlank(message = "농가 설명을 입력해주세요.")
    @Size(min = 10, max = 255, message = "농가 설명은 10자 이상 255자 이하여야 합니다.")
    private String farmerDescription;

    public CreateFarmerCommand toCommand() {
        return new CreateFarmerCommand(super.getEmail(), super.getPassword(), super.getNickname(),
                super.getProfileImage(), super.getRole(), farmerLicenseNumber, farmName, farmerDescription);
    }
}
