package com.example.lettuce.domain.carbonfootprint.service;

import java.math.BigDecimal;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintProductResponse;

import lombok.Getter;

@Getter
public class CarbonFootPrintProduct {
    private Long id;
    private String name;
    private String productUrl;
    private String thumbnailImageUrl;
    private BigDecimal carbonFootprint;    

    public static CarbonFootPrintProduct of(String name, String productUrl, String thumbnailImageUrl,
            BigDecimal carbonFootprint) {
        return new CarbonFootPrintProduct(name, productUrl, thumbnailImageUrl, carbonFootprint);
    }

    public static CarbonFootPrintProduct of(CarbonFootprintProductResponse response) {
        return new CarbonFootPrintProduct(response.getName(), response.getProductUrl(), response.getThumbnailImageUrl(),
                response.getCarbonFootprint());
    }

    public CarbonFootPrintProduct(String name, String productUrl, String thumbnailImageUrl,
            BigDecimal carbonFootprint) {
        this(null, name, productUrl, thumbnailImageUrl, carbonFootprint);
    }

    public CarbonFootPrintProduct(Long id, String name, String productUrl, String thumbnailImageUrl,
            BigDecimal carbonFootprint) {
        this.id = id;
        this.name = name;
        this.productUrl = productUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.carbonFootprint = carbonFootprint;
    }
}
