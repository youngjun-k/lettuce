package com.example.lettuce.global.shared.mapper;

import org.mapstruct.Mapper;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintResponse;
import com.example.lettuce.domain.carbonfootprint.entity.CarbonFootPrint;

@Mapper(componentModel = "spring")
public interface CarbonFootPrintMapper {

    CarbonFootPrint toEntity(CarbonFootprintResponse response, Long userId, String imageUrl);

    CarbonFootprintResponse toResponse(CarbonFootPrint entity);
}
