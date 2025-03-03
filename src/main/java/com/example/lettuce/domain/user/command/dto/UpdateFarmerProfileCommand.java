package com.example.lettuce.domain.user.command.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateFarmerProfileCommand extends UpdateUserCommand {

    @NotBlank(message = "농가 면허번호를 입력해주세요.")
    private String farmerLicenseNumber;

    @NotBlank(message = "농가 설명을 입력해주세요.")
    private String farmerDescription;

    public UpdateFarmerProfileCommand(String nickname, MultipartFile profileImage, String farmerLicenseNumber,
            String farmerDescription) {
        super(nickname, profileImage);
        this.farmerLicenseNumber = farmerLicenseNumber;
        this.farmerDescription = farmerDescription;
    }
}
