package com.example.lettuce.domain.carbonfootprint.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintProductResponse;
import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintRewardResponse;
import com.example.lettuce.domain.carbonfootprint.service.CarbonFootprintService;
import com.example.lettuce.domain.user.entity.User;
import com.example.lettuce.global.framework.security.annotation.LoginUser;
import com.example.lettuce.global.shared.exception.code.SuccessCode;
import com.example.lettuce.global.shared.response.CommonResponse;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carbon-footprint")
public class CarbonFootPrintController {

    private final CarbonFootprintService carbonFootprintService;

    @PostMapping
    public ResponseEntity<CommonResponse<CarbonFootprintRewardResponse>> calculateFootprintByImage(
            @RequestPart(name = "image") MultipartFile image,
            @LoginUser User user) {
        return CommonResponse.success(SuccessCode.SUCCESS,
                carbonFootprintService.calculateFootprintByImage(image, user));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<CarbonFootprintProductResponse>> calculateFootprintByUrl(
            @RequestParam String url) {
        return CommonResponse.success(SuccessCode.SUCCESS,
                carbonFootprintService.calculateFootprintByUrl(url));
    }

    @GetMapping(value = "/product/{productName}", params = { "page", "size" })
    public ResponseEntity<CommonResponse<Page<CarbonFootprintRewardResponse>>> calculateFootprintByCoupang(
            @PathVariable String productName,
            @RequestParam(defaultValue = "1") @Min(value = 1) int page,
            @RequestParam(defaultValue = "10") @Min(value = 10) @Max(value = 100) int size) {
            return null;    
    }

}
