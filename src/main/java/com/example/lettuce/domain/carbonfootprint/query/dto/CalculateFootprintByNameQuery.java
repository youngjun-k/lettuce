package com.example.lettuce.domain.carbonfootprint.query.dto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.lettuce.api.carbonfootprint.dto.response.CarbonFootprintProductResponse;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.cqrs.Query;

public record CalculateFootprintByNameQuery(
        String productName,
        PageRequest pageRequest,
        User user) implements Query<Page<CarbonFootprintProductResponse>> {
}