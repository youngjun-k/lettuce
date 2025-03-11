package com.example.lettuce.domain.user.command.dto;

import com.example.lettuce.domain.user.aggregate.enums.UserRole;

import lombok.Getter;

@Getter
public class CreateFarmerCommand extends CreateUserCommand {

    public CreateFarmerCommand(String email, String password, String nickname, String profileImage, UserRole role,
            String farmerLicenseNumber, String farmName, String farmerDescription) {
        super(email, password, nickname, profileImage, role);
        this.farmerLicenseNumber = farmerLicenseNumber;
        this.farmName = farmName;
        this.farmerDescription = farmerDescription;
    }

    private String farmerLicenseNumber;

    private String farmName;

    private String farmerDescription;
}
