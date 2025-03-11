package com.example.lettuce.domain.user.command.dto;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import com.example.lettuce.domain.user.aggregate.enums.UserRole;

public class CreatePartnerCommand extends CreateUserCommand {

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

    public CreatePartnerCommand(String email, String password, String nickname, String profileImage, UserRole role,
            String partnerType, String partnerRegistrationNumber, String partnerDescription, String partnerWebsite,
            String partnerPhone, String partnerAddress) {
        super(email, password, nickname, profileImage, role);
        this.partnerType = partnerType;
        this.partnerRegistrationNumber = partnerRegistrationNumber;
        this.partnerDescription = partnerDescription;
        this.partnerWebsite = partnerWebsite;
        this.partnerPhone = partnerPhone;
        this.partnerAddress = partnerAddress;
    }
}
