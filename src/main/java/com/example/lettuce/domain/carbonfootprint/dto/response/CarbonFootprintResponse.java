package com.example.lettuce.domain.carbonfootprint.dto.response;

import java.time.LocalDateTime;

public record CarbonFootprintResponse(
                String itemCategory,
                String itemName,
                String description,
                String savedCarbonFootprint,
                int awardedPoint,
                LocalDateTime usedAt,
                LocalDateTime createdAt) {
}