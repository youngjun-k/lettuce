package com.example.lettuce.domain.user.command.dto;

import org.springframework.web.multipart.MultipartFile;


import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdatePartnerProfileCommand extends UpdateUserCommand {

    public UpdatePartnerProfileCommand(String nickname, MultipartFile profileImage, String partnerDescription,
            String partnerWebsite, String partnerPhone, String partnerAddress) {
        super(nickname, profileImage);
        this.partnerDescription = partnerDescription;
        this.partnerWebsite = partnerWebsite;
        this.partnerPhone = partnerPhone;
        this.partnerAddress = partnerAddress;
    }

    @NotBlank(message = "파트너 설명을 입력해주세요.")
    private String partnerDescription;

    @Nullable
    private String partnerWebsite;

    @NotBlank(message = "파트너 전화번호를 입력해주세요.")
    private String partnerPhone;

    @NotBlank(message = "파트너 주소를 입력해주세요.")
    private String partnerAddress;
}
