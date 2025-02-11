package com.example.lettuce.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lettuce.domain.user.dto.request.UpdateClientProfileRequest;
import com.example.lettuce.domain.user.dto.request.UpdateFarmerProfileRequest;
import com.example.lettuce.domain.user.dto.request.UpdatePartnerProfileRequest;
import com.example.lettuce.domain.user.dto.response.ProfileResponse;
import com.example.lettuce.domain.user.entity.User;
import com.example.lettuce.domain.user.service.ProfileService;
import com.example.lettuce.global.shared.response.CommonResponse;
import com.example.lettuce.global.shared.response.VoidResponse;

import jakarta.validation.Valid;

import com.example.lettuce.global.framework.security.annotation.LoginUser;
import com.example.lettuce.global.shared.exception.code.SuccessCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<CommonResponse<ProfileResponse>> getProfile(
            @LoginUser User user) {
        return CommonResponse.success(SuccessCode.SUCCESS, profileService.getProfile(user));
    }

    @PatchMapping("/client")
    public ResponseEntity<CommonResponse<VoidResponse>> updateClientProfile(
            @LoginUser User user,
            @Valid @RequestBody UpdateClientProfileRequest profileRequest) {
        profileService.updateClientProfile(user, profileRequest);
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE);
    }

    @PatchMapping("/partner")
    public ResponseEntity<CommonResponse<VoidResponse>> updatePartnerProfile(
            @LoginUser User user,
            @Valid @RequestBody UpdatePartnerProfileRequest profileRequest) {
        profileService.updatePartnerProfile(user, profileRequest);
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE);
    }

    @PatchMapping("/farmer")
    public ResponseEntity<CommonResponse<VoidResponse>> updateFarmerProfile(
            @LoginUser User user,
            @Valid @RequestBody UpdateFarmerProfileRequest profileRequest) {
        profileService.updateFarmerProfile(user, profileRequest);
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE);
    }

}
