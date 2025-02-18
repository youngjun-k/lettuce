package com.example.lettuce.domain.carbonfootprint.service;

import java.math.BigDecimal;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintProductResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class CarbonFootPrintProduct {
    private Long id;

    @NotNull(message = "사용자 ID는 필수 입력 사항입니다.")
    private Long userId;

    @NotBlank(message = "상품명은 필수 입력 사항입니다.")
    private String name;

    @NotBlank(message = "상품 URL은 필수 입력 사항입니다.")
    private String productUrl;

    @NotBlank(message = "썸네일 이미지 URL은 필수 입력 사항입니다.")
    private String thumbnailImageUrl;

    @NotNull(message = "탄소 배출량은 필수 입력 사항입니다.")
    @PositiveOrZero(message = "탄소 배출량은 0 이상이어야 합니다.")
    private BigDecimal carbonFootprint;

    public static CarbonFootPrintProduct of(Long userId, String name, String productUrl, String thumbnailImageUrl,
            BigDecimal carbonFootprint) {
        return new CarbonFootPrintProduct(userId, name, productUrl, thumbnailImageUrl, carbonFootprint);
    }

    public static CarbonFootPrintProduct of(Long userId, CarbonFootprintProductResponse response) {
        return new CarbonFootPrintProduct(userId, response.getName(), response.getProductUrl(),
                response.getThumbnailImageUrl(), response.getCarbonFootprint());
    }

    public CarbonFootPrintProduct(Long userId, String name, String productUrl, String thumbnailImageUrl,
            BigDecimal carbonFootprint) {
        this(null, userId, name, productUrl, thumbnailImageUrl, carbonFootprint);
    }

    public CarbonFootPrintProduct(Long id, Long userId, String name, String productUrl, String thumbnailImageUrl,
            BigDecimal carbonFootprint) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.productUrl = productUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.carbonFootprint = carbonFootprint;
    }
}
