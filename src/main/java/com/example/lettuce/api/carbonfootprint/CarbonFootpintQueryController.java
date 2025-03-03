package com.example.lettuce.api.carbonfootprint;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lettuce.api.carbonfootprint.dto.response.CarbonFootprintProductResponse;
import com.example.lettuce.domain.carbonfootprint.query.CarbonFootprintQueryService;
import com.example.lettuce.domain.carbonfootprint.query.dto.CalculateFootprintByNameQuery;
import com.example.lettuce.domain.carbonfootprint.query.dto.CalculateFootprintByUrlQuery;
import com.example.lettuce.domain.carbonfootprint.query.dto.FindFootprintByUserIdQuery;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.security.annotation.LoginUser;
import com.example.lettuce.global.shared.exception.code.SuccessCode;
import com.example.lettuce.global.shared.response.CommonResponse;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carbon-footprint")
public class CarbonFootpintQueryController {

        private final CarbonFootprintQueryService queryService;

        @GetMapping
        public ResponseEntity<CommonResponse<CarbonFootprintProductResponse>> calculateFootprintByUrl(
                        @RequestParam(required = true) @URL(message = "유효하지 않은 URL입니다.") String url,
                        @LoginUser User user) {
                return CommonResponse.success(SuccessCode.SUCCESS,
                                queryService.calculateFootprintByUrl(new CalculateFootprintByUrlQuery(url, user)));
        }

        @GetMapping(value = "/product/{productName}", params = { "page", "size" })
        public ResponseEntity<CommonResponse<Page<CarbonFootprintProductResponse>>> calculateFootprintByProductName(
                        @PathVariable String productName,
                        @RequestParam(defaultValue = "1") @Min(value = 1) int page,
                        @RequestParam(defaultValue = "10") @Min(value = 10) @Max(value = 100) int size,
                        @LoginUser User user) {
                return CommonResponse.success(SuccessCode.SUCCESS,
                                queryService.calculateFootprintByName(new CalculateFootprintByNameQuery(productName,
                                                PageRequest.of(page - 1, size), user)));
        }

        @GetMapping(value = "/my/product")
        public ResponseEntity<CommonResponse<List<CarbonFootprintProductResponse>>> findFootprintByUserId(
                        @LoginUser User user) {
                return CommonResponse.success(SuccessCode.SUCCESS,
                                queryService.findFootprintByUserId(new FindFootprintByUserIdQuery(user)));
        }

}
