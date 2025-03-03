package com.example.lettuce.api.user;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.lettuce.api.user.dto.request.CreateClientRequest;
import com.example.lettuce.api.user.dto.request.CreateFarmerRequest;
import com.example.lettuce.api.user.dto.request.CreatePartnerRequest;
import com.example.lettuce.api.user.dto.request.DeleteAccountRequest;
import com.example.lettuce.api.user.dto.request.LoginRequest;
import com.example.lettuce.api.user.dto.request.ResetPasswordRequest;
import com.example.lettuce.api.user.dto.request.UpdateClientProfileRequest;
import com.example.lettuce.api.user.dto.request.UpdateFarmerProfileRequest;
import com.example.lettuce.api.user.dto.request.UpdatePartnerProfileRequest;
import com.example.lettuce.api.user.dto.response.AuthResponse;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.command.UserCommandService;
import com.example.lettuce.domain.user.command.dto.ResetPasswordCommand;
import com.example.lettuce.domain.user.command.dto.VerifyEmailCommand;
import com.example.lettuce.global.framework.security.annotation.LoginUser;
import com.example.lettuce.global.shared.exception.code.SuccessCode;
import com.example.lettuce.global.shared.response.CommonResponse;
import com.example.lettuce.global.shared.response.VoidResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserCommandController {

    private final UserCommandService commandService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return CommonResponse.success(SuccessCode.SUCCESS, commandService.login(request.toCommand()));
    }

    @PostMapping("/client")
    public ResponseEntity<CommonResponse<VoidResponse>> createUser(@Valid @RequestBody CreateClientRequest request) {
        commandService.registerUser(request.toCommand());
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT);
    }

@PostMapping("/farmer")
    public ResponseEntity<CommonResponse<VoidResponse>> createFarmer(@Valid @RequestBody CreateFarmerRequest request) {
        commandService.registerUser(request.toCommand());
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT);
    }

    @PostMapping("/partner")
    public ResponseEntity<CommonResponse<VoidResponse>> createPartner(
            @Valid @RequestBody CreatePartnerRequest request) {
        commandService.registerUser(request.toCommand());
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT);
    }

    @PutMapping(value = "/client", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CommonResponse<VoidResponse>> updateClient(
            @RequestPart(name = "image", required = false) MultipartFile profileImage,
            @RequestPart(name = "request") @Valid UpdateClientProfileRequest request) {
        commandService.updateProfile(request.toCommand(profileImage));
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE);
    }

    @PutMapping(value = "/farmer", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CommonResponse<VoidResponse>> updateFarmer(
            @RequestPart(name = "image", required = false) MultipartFile profileImage,
            @RequestPart(name = "request") @Valid UpdateFarmerProfileRequest request) {
        commandService.updateProfile(request.toCommand(profileImage));
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE);
    }

    @PutMapping(value = "/partner", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CommonResponse<VoidResponse>> updatePartner(
            @RequestPart(name = "image", required = false) MultipartFile profileImage,
            @RequestPart(name = "request") @Valid UpdatePartnerProfileRequest request) {
        commandService.updateProfile(request.toCommand(profileImage));
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE);
    }

    @PatchMapping("/verify-email")
    public ResponseEntity<CommonResponse<VoidResponse>> verifyEmail(@RequestParam @NotBlank String token) {
        commandService.verifyEmail(new VerifyEmailCommand(token));
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<CommonResponse<VoidResponse>> resetPassword(@RequestParam @NotBlank String token,
            @RequestBody @Valid ResetPasswordRequest request) {
        commandService.resetPassword(new ResetPasswordCommand(token, request.newPassword()));
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE);
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse<VoidResponse>> deleteUser(
            @Valid @RequestBody DeleteAccountRequest request,
            @LoginUser User user) {
        commandService.deleteAccount(request.toCommand(user));
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE);
    }
}
