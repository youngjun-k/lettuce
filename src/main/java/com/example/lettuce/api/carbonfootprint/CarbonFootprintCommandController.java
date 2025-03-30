package com.example.lettuce.api.carbonfootprint;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.lettuce.api.carbonfootprint.dto.response.CarbonFootprintRewardResponse;
import com.example.lettuce.domain.carbonfootprint.command.CarbonFootprintCommandService;
import com.example.lettuce.domain.carbonfootprint.command.dto.CalculateFootprintByImageCommand;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.security.annotation.LoginUser;
import com.example.lettuce.global.infrastructure.storage.ColumnarStorageService;
import com.example.lettuce.global.shared.exception.code.SuccessCode;
import com.example.lettuce.global.shared.response.CommonResponse;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carbon-footprint")
public class CarbonFootprintCommandController {

        private final CarbonFootprintCommandService commandService;
        private final ColumnarStorageService columnarStorageService;

        @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseEntity<CommonResponse<CarbonFootprintRewardResponse>> calculateFootprintByImage(
                        @RequestPart(name = "image", required = true) @Valid MultipartFile image,
                        @LoginUser User user) {
                return CommonResponse.success(SuccessCode.SUCCESS,
                                commandService.calculateFootprintByImage(new CalculateFootprintByImageCommand(image,
                                                user)));
        }

        @PostMapping(value = "/export")
        public ResponseEntity<CommonResponse<String>> exportCarbonFootprintData(
                        @RequestParam(name = "startDate", required = true) @Valid LocalDateTime startDate,
                        @RequestParam(name = "endDate", required = true) @Valid LocalDateTime endDate)
                        throws IOException {
                return CommonResponse.success(SuccessCode.SUCCESS,
                                columnarStorageService.exportCarbonFootprintData(startDate, endDate));
        }

}
