package com.example.lettuce.domain.carbonfootprint.dto.response;

public record CarbonFootprintProductResponse(
        String name,
        String url,
        String thumbnailImageUrl,
        String carbonFootprint) {
}
