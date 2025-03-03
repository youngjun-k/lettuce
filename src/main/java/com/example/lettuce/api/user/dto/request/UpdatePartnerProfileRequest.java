package com.example.lettuce.api.user.dto.request;

import com.example.lettuce.domain.user.command.dto.UpdatePartnerProfileCommand;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
@Getter
public class UpdatePartnerProfileRequest extends UpdateUserRequest {

    @NotBlank(message = "파트너 설명을 입력해주세요.")
    private String partnerDescription;

    @Nullable
    private String partnerWebsite;

    @NotBlank(message = "파트너 전화번호를 입력해주세요.")
    private String partnerPhone;

    @NotBlank(message = "파트너 주소를 입력해주세요.")
    private String partnerAddress;

    public UpdatePartnerProfileCommand toCommand(MultipartFile profileImage) {
        return new UpdatePartnerProfileCommand(super.getNickname(), profileImage, partnerDescription,
                partnerWebsite, partnerPhone, partnerAddress);
    }
}
