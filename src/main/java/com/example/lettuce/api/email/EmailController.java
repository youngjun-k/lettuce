package com.example.lettuce.api.email;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lettuce.global.shared.response.CommonResponse;
import com.example.lettuce.global.shared.response.VoidResponse;
import com.example.lettuce.global.shared.exception.code.SuccessCode;
import com.example.lettuce.api.email.dto.request.EmailResetPasswordRequest;
import com.example.lettuce.api.email.dto.request.EmailVerifyRequest;
import com.example.lettuce.domain.email.EmailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send/reset-password")
    public ResponseEntity<CommonResponse<VoidResponse>> sendResetPasswordEmail(
            @Valid @RequestBody EmailResetPasswordRequest request) {
        emailService.sendResetPasswordEmail(request);
        return CommonResponse.success(SuccessCode.SUCCESS);
    }

    @PostMapping("/send/verify-email")
    public ResponseEntity<CommonResponse<VoidResponse>> sendVerifyEmail(
            @Valid @RequestBody EmailVerifyRequest request) {
        emailService.sendVerifyEmail(request);
        return CommonResponse.success(SuccessCode.SUCCESS);
    }

}
