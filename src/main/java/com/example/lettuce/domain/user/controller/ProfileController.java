package com.example.lettuce.domain.user.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping(value = "/client", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CommonResponse<VoidResponse>> updateClientProfile(
            @RequestPart(name = "image", required = false) MultipartFile profileImage,
            @RequestPart(name = "request") @Valid UpdateClientProfileRequest profileRequest,
            @LoginUser User user) {
        profileService.updateClientProfile(user, profileRequest, profileImage);
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE);
    }

    @PutMapping(value = "/partner", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CommonResponse<VoidResponse>> updatePartnerProfile(
            @RequestPart(name = "image", required = false) MultipartFile profileImage,
            @RequestPart(name = "request") @Valid UpdatePartnerProfileRequest profileRequest,
            @LoginUser User user) {
        profileService.updatePartnerProfile(user, profileRequest, profileImage);
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE);
    }

    @PutMapping(value = "/farmer", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CommonResponse<VoidResponse>> updateFarmerProfile(
            @RequestPart(name = "image", required = false) MultipartFile profileImage,
            @RequestPart(name = "request") @Valid UpdateFarmerProfileRequest profileRequest,
            @LoginUser User user) {
        profileService.updateFarmerProfile(user, profileRequest, profileImage);
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE);
    }

}
