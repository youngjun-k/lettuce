package com.example.lettuce.domain.carbonfootprint.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintResponse;
import com.example.lettuce.domain.carbonfootprint.service.CarbonFootprintService;
import com.example.lettuce.domain.user.entity.User;
import com.example.lettuce.global.framework.security.annotation.LoginUser;
import com.example.lettuce.global.shared.exception.code.SuccessCode;
import com.example.lettuce.global.shared.response.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carbon-footprint")
public class CarbonFootPrintController {

    private final CarbonFootprintService carbonFootprintService;

    @PostMapping
    public ResponseEntity<CommonResponse<CarbonFootprintResponse>> calculateFootprint(
            @RequestPart(name = "image") MultipartFile image,
            @LoginUser User user) {

        return CommonResponse.success(SuccessCode.SUCCESS,
                carbonFootprintService.calculateFootprint(image, user));
    }

}
