package com.example.lettuce.domain.carbonfootprint.mapper;

import org.mapstruct.Mapper;

import com.example.lettuce.api.carbonfootprint.dto.response.CarbonFootprintRewardResponse;
import com.example.lettuce.domain.carbonfootprint.aggregate.RewardHistory;

@Mapper(componentModel = "spring")
public interface CarbonFootPrintMapper {

    RewardHistory toEntity(CarbonFootprintRewardResponse response, Long userId, String imageUrl);

    CarbonFootprintRewardResponse toResponse(RewardHistory entity);
}
