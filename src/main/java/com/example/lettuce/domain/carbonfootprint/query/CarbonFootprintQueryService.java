package com.example.lettuce.domain.carbonfootprint.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.lettuce.api.carbonfootprint.dto.response.CarbonFootprintProductResponse;
import com.example.lettuce.domain.carbonfootprint.query.dto.CalculateFootprintByNameQuery;
import com.example.lettuce.domain.carbonfootprint.query.dto.CalculateFootprintByUrlQuery;
import com.example.lettuce.domain.carbonfootprint.query.dto.FindFootprintByUserIdQuery;
import com.example.lettuce.global.framework.cqrs.QueryBus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarbonFootprintQueryService {

    private final QueryBus queryBus;

    public CarbonFootprintProductResponse calculateFootprintByUrl(CalculateFootprintByUrlQuery query) {
        return queryBus.execute(query);
    }

    public Page<CarbonFootprintProductResponse> calculateFootprintByName(CalculateFootprintByNameQuery query) {
        return queryBus.execute(query);
    }

    public List<CarbonFootprintProductResponse> findFootprintByUserId(FindFootprintByUserIdQuery query) {
        return queryBus.execute(query);
    }
}
