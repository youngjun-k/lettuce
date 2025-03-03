package com.example.lettuce.domain.carbonfootprint.query.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.example.lettuce.api.carbonfootprint.dto.response.CarbonFootprintProductResponse;
import com.example.lettuce.domain.carbonfootprint.aggregate.CarbonFootPrintProduct;
import com.example.lettuce.domain.carbonfootprint.query.dto.CalculateFootprintByNameQuery;
import com.example.lettuce.domain.carbonfootprint.repository.CarbonFootprintProductRepository;
import com.example.lettuce.global.framework.cqrs.QueryHandler;
import com.example.lettuce.global.shared.async.AsyncEventProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CalculateFootprintByNameQueryHandler
        implements QueryHandler<CalculateFootprintByNameQuery, Page<CarbonFootprintProductResponse>> {

    private final CarbonFootprintProductRepository carbonFootprintProductRepository;
    private final AsyncEventProducer<CarbonFootPrintProduct> asyncEventProducer;

    /**
     * Calculate Carbon Footprint by Name.
     * <p>
     * This method retrieves a list of CarbonFootprintProductResponse by name from
     * the crawler
     * and maps it to a Page of CarbonFootprintProductResponse.
     * </p>
     * 
     * @param name     the name of the product
     * @param pageable the pageable object
     * @param user     the current user performing the operation
     * @return a Page of CarbonFootprintProductResponse containing product details
     *         and
     *         carbon footprint
     */
    @Cacheable(value = "carbon_footprint_product_by_product_name", key = "#name")
    @Override
    public Page<CarbonFootprintProductResponse> handle(CalculateFootprintByNameQuery query) {
        Page<CarbonFootprintProductResponse> responses = carbonFootprintProductRepository
                .findByName(query.productName(), query.pageRequest());

        if (responses.isEmpty()) {
            return Page.empty();
        }
        List<CarbonFootPrintProduct> carbonFootprintProducts = responses.stream()
                .map(res -> CarbonFootPrintProduct.of(query.user().getId(), res))
                .collect(Collectors.toList());

        asyncEventProducer.produce(carbonFootprintProducts);
        return responses;
    }

}
