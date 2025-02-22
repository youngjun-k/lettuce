package com.example.lettuce.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.lettuce.domain.auth.dto.request.CreateClientRequest;
import com.example.lettuce.domain.auth.dto.request.CreateFarmerRequest;
import com.example.lettuce.domain.auth.dto.request.CreatePartnerRequest;
import com.example.lettuce.domain.auth.dto.request.LoginRequest;
import com.example.lettuce.domain.auth.dto.request.ResetPasswordRequest;
import com.example.lettuce.domain.auth.dto.response.AuthResponse;
import com.example.lettuce.domain.auth.service.AuthService;
import com.example.lettuce.domain.user.dao.User;
import com.example.lettuce.domain.user.enums.UserRole;
import com.example.lettuce.global.shared.response.CommonResponse;
import com.example.lettuce.global.shared.response.VoidResponse;
import com.example.lettuce.global.framework.ratelimit.annotation.RateLimitType;
import com.example.lettuce.global.framework.ratelimit.annotation.RateLimited;
import com.example.lettuce.global.framework.security.annotation.LoginUser;
import com.example.lettuce.global.shared.exception.code.SuccessCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @RateLimited(type = RateLimitType.USER, key = "#p0.email")
    public ResponseEntity<CommonResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, authService.login(request));
    }

    @PostMapping("/register/client")
    public ResponseEntity<CommonResponse<VoidResponse>> registerClient(
            @Valid @RequestBody CreateClientRequest request) {

        authService.registerUser(request, UserRole.CLIENT);
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT);
    }

    @PostMapping("/register/partner")
    public ResponseEntity<CommonResponse<VoidResponse>> registerPartner(
            @Valid @RequestBody CreatePartnerRequest request) {
        authService.registerUser(request, UserRole.BUSINESS_PARTNER);
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT);
    }

    @PostMapping("/register/farmer")
    public ResponseEntity<CommonResponse<VoidResponse>> registerFarmer(
            @Valid @RequestBody CreateFarmerRequest request) {
        authService.registerUser(request, UserRole.FARMER);
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<CommonResponse<AuthResponse>> verifyEmail(
            @RequestParam(value = "token", required = true) String token) {
        return CommonResponse.success(SuccessCode.SUCCESS, authService.verifyEmail(token));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<CommonResponse<VoidResponse>> resetPassword(
            @RequestParam(value = "token", required = true) String token,
            @Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(token, request);
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE);
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<CommonResponse<VoidResponse>> deleteAccount(
            @LoginUser User user,
            @Valid @RequestBody DeleteAccountRequest request) {
        authService.deleteAccount(user, request);
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }

}
