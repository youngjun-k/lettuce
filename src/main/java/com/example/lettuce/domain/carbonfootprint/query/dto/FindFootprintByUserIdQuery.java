package com.example.lettuce.domain.carbonfootprint.query.dto;

import java.util.List;

import com.example.lettuce.api.carbonfootprint.dto.response.CarbonFootprintProductResponse;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.global.framework.cqrs.Query;

public record FindFootprintByUserIdQuery(User user) implements Query<List<CarbonFootprintProductResponse>> {

}
