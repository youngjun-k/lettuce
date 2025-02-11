package com.example.lettuce.domain.auth.dto.request;

import com.example.lettuce.global.framework.security.annotation.ValidNickname;
import com.example.lettuce.global.framework.security.validator.ValidPassword;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public abstract class CreateUserRequest {

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;

        @ValidPassword
        private String password;

        @ValidNickname
        private String nickname;

        @Nullable
        private String profileImage;
}
