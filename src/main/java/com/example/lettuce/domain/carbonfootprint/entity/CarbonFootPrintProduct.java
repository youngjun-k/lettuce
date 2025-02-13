package com.example.lettuce.domain.carbonfootprint.entity;

import com.example.lettuce.global.shared.entity.BaseTime;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class CarbonFootPrintProduct extends BaseTime {

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255) COMMENT '상품 이름'")
    private String name;

    @Column(name = "url", nullable = false, columnDefinition = "VARCHAR(255) COMMENT '상품 URL'")
    private String url;

    @Column(name = "thumbnail_image_url", nullable = false, columnDefinition = "VARCHAR(255) COMMENT '상품 썸네일 이미지 URL'")
    private String thumbnailImageUrl;

    @Column(name = "carbon_footprint", nullable = false, columnDefinition = "INT COMMENT '상품 Co2 배출량'")
    private String carbonFootprint;
}
