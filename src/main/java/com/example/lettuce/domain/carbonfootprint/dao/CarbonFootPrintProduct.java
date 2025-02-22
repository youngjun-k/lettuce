package com.example.lettuce.domain.carbonfootprint.dao;

import java.math.BigDecimal;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintProductResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CarbonFootPrintProduct {
    private final Long id;

    @NotNull(message = "사용자 ID는 필수 입력 사항입니다.")
    private final Long userId;

    @NotBlank(message = "상품명은 필수 입력 사항입니다.")
    private final String name;

    @NotBlank(message = "상품 URL은 필수 입력 사항입니다.")
    private final String productUrl;

    @NotBlank(message = "썸네일 이미지 URL은 필수 입력 사항입니다.")
    private final String thumbnailImageUrl;

    @NotNull(message = "탄소 배출량은 필수 입력 사항입니다.")
    @PositiveOrZero(message = "탄소 배출량은 0 이상이어야 합니다.")
    private final BigDecimal carbonFootprint;

    public static CarbonFootPrintProduct of(Long userId, String name, String productUrl, String thumbnailImageUrl,
            BigDecimal carbonFootprint) {
        return builder()
                .userId(userId)
                .name(name)
                .productUrl(productUrl)
                .thumbnailImageUrl(thumbnailImageUrl)
                .carbonFootprint(carbonFootprint)
                .build();
    }

    public static CarbonFootPrintProduct of(Long userId, CarbonFootprintProductResponse response) {
        return builder()
                .userId(userId)
                .name(response.getName())
                .productUrl(response.getProductUrl())
                .thumbnailImageUrl(response.getThumbnailImageUrl())
                .carbonFootprint(response.getCarbonFootprint())
                .build();
    }
}
