package com.example.lettuce.api.user.dto.request;

import com.example.lettuce.domain.user.aggregate.enums.UserRole;
import com.example.lettuce.global.framework.security.annotation.ValidNickname;
import com.example.lettuce.global.framework.security.annotation.ValidPassword;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

        @NotNull(message = "사용자 역할을 입력해주세요.")
        private UserRole role;

}
