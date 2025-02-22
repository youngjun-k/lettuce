package com.example.lettuce.domain.user.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.lettuce.domain.user.dao.ClientProfile;
import com.example.lettuce.global.framework.security.annotation.Age;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateClientProfileRequest extends UpdateUserProfileRequest<ClientProfile> {

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @Age(min = 14, message = "14세 이상만 가입이 가능합니다.")
        private LocalDate birthday;

        @NotBlank(message = "성별을 입력해주세요.")
        @Pattern(regexp = "^[가-힣]{1}$", message = "성별은 한글 한 글자여야 합니다.")
        private String gender;

}
