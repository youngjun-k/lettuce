package com.example.lettuce.domain.carbonfootprint.mapper;

import org.mapstruct.Mapper;

import com.example.lettuce.domain.carbonfootprint.dao.CarbonFootPrintReward;
import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintRewardResponse;

@Mapper(componentModel = "spring")
public interface CarbonFootPrintMapper {

    CarbonFootPrintReward toEntity(CarbonFootprintRewardResponse response, Long userId, String imageUrl);

    CarbonFootprintRewardResponse toResponse(CarbonFootPrintReward entity);
}
