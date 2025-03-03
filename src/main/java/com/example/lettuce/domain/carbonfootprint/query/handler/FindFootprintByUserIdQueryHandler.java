package com.example.lettuce.domain.carbonfootprint.query.handler;

import org.springframework.stereotype.Component;

import com.example.lettuce.api.carbonfootprint.dto.response.CarbonFootprintProductResponse;
import com.example.lettuce.domain.carbonfootprint.query.dto.FindFootprintByUserIdQuery;
import com.example.lettuce.domain.carbonfootprint.repository.AsyncCarbonFootprintProductRepository;
import com.example.lettuce.global.framework.cqrs.QueryHandler;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FindFootprintByUserIdQueryHandler
        implements QueryHandler<FindFootprintByUserIdQuery, List<CarbonFootprintProductResponse>> {
    private final AsyncCarbonFootprintProductRepository asyncCarbonFootprintProductRepository;

    /**
     * Find Carbon Footprint by User ID.
     * <p>
     * This method retrieves a list of CarbonFootprintProductResponse by user ID
     * from the crawler
     * and maps it to a List of CarbonFootprintProductResponse.
     * </p>
     * 
     * @param userId the ID of the user
     * @return a List of CarbonFootprintProductResponse containing product details
     *         and
     *         carbon footprint
     */
    @Override
    public List<CarbonFootprintProductResponse> handle(FindFootprintByUserIdQuery query) {
        List<CarbonFootprintProductResponse> responses = asyncCarbonFootprintProductRepository
                .findByProductByUserId(query.user().getId());

        return responses;
    }

}
