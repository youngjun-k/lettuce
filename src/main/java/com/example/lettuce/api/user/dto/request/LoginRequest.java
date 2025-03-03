package com.example.lettuce.api.user.dto.request;

import com.example.lettuce.domain.user.command.dto.LoginCommand;
import com.example.lettuce.global.framework.security.annotation.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
                @NotBlank(message = "이메일을 입력해주세요.") @Email(message = "이메일 형식이 올바르지 않습니다.") String email,

                @ValidPassword String password) {

        public LoginCommand toCommand() {
                return new LoginCommand(email, password);
        }
}
