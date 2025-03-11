package com.example.lettuce.api.carbonfootprint.dto.response;

import java.math.BigDecimal;

public record CarbonFootprintRewardResponse(
        String productName,
        String productCategory,
        String description,
        BigDecimal carbonValue,
        BigDecimal carbonReduction,
        String environmentalImpact) {
}