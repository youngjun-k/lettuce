package com.example.lettuce.domain.carbonfootprint.query.dto;

import com.example.lettuce.api.carbonfootprint.dto.response.CarbonFootprintProductResponse;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.cqrs.Query;

public record CalculateFootprintByUrlQuery(String url, User user) implements Query<CarbonFootprintProductResponse> {
}
