package com.example.lettuce.api.carbonfootprint.dto.response;

import java.time.LocalDateTime;

public record CarbonFootprintRewardResponse(
                String itemCategory,
                String itemName,
                String description,
                String savedCarbonFootprint,
                int awardedPoint,
                LocalDateTime usedAt,
                LocalDateTime createdAt) {
}