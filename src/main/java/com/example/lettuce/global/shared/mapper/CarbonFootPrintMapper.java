package com.example.lettuce.global.shared.mapper;

import org.mapstruct.Mapper;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintRewardResponse;
import com.example.lettuce.domain.carbonfootprint.entity.CarbonFootPrintReward;

@Mapper(componentModel = "spring")
public interface CarbonFootPrintMapper {

    CarbonFootPrintReward toEntity(CarbonFootprintRewardResponse response, Long userId, String imageUrl);

    CarbonFootprintRewardResponse toResponse(CarbonFootPrintReward entity);
}
