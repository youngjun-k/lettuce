package com.example.lettuce.domain.user.aggregate;

import com.example.lettuce.domain.user.command.dto.UpdatePartnerProfileCommand;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(name = "partner_profiles")
public class PartnerProfile extends Profile implements ProfileUpdatable<UpdatePartnerProfileCommand> {

    @Column(name = "partner_type", length = 45, nullable = false, columnDefinition = "varchar(45) comment '파트너 유형'")
    private String partnerType;

    @Column(name = "partner_registration_number", length = 45, nullable = false, columnDefinition = "varchar(45) comment '파트너 사업자 등록번호'")
    private String partnerRegistrationNumber;

    @Column(name = "partner_description", length = 255, nullable = false, columnDefinition = "varchar(255) comment '파트너 설명'")
    private String partnerDescription;

    @Column(name = "partner_phone", length = 20, nullable = false, columnDefinition = "varchar(20) comment '파트너 전화번호'")
    private String partnerPhone;

    @Column(name = "partner_address", length = 255, nullable = false, columnDefinition = "varchar(255) comment '파트너 주소'")
    private String partnerAddress;

    @Override
    public void updateProfile(UpdatePartnerProfileCommand profileCommand) {
        super.updateBaseProfile(profileCommand);
        this.partnerDescription = profileCommand.getPartnerDescription();
        this.partnerPhone = profileCommand.getPartnerPhone();
        this.partnerAddress = profileCommand.getPartnerAddress();
    }
}
