package com.example.lettuce.domain.user.aggregate;

import com.example.lettuce.domain.user.command.dto.UpdateFarmerProfileCommand;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(name = "farmer_profiles")
public class FarmerProfile extends Profile implements ProfileUpdatable<UpdateFarmerProfileCommand> {

    @Column(name = "farmer_license_number", length = 45, nullable = false, columnDefinition = "varchar(45) comment '농가 면허번호'")
    @Pattern(regexp = "^[0-9]{8}$", message = "올바른 면허번호 형식이 아닙니다.")
    private String farmerLicenseNumber;

    @Column(name = "farm_name", length = 45, nullable = false, columnDefinition = "varchar(45) comment '농장 이름'")
    private String farmName;

    @Column(name = "farmer_description", length = 255, nullable = false, columnDefinition = "varchar(255) comment '농가 설명'")
    @Size(min = 10, max = 255, message = "농가 설명은 최소 10자, 최대 255자입니다.")
    private String farmerDescription;

    @Override
    public void updateProfile(UpdateFarmerProfileCommand profileCommand) {        
        super.updateBaseProfile(profileCommand);
        this.farmerLicenseNumber = profileCommand.getFarmerLicenseNumber();
        this.farmerDescription = profileCommand.getFarmerDescription();
    }

}
