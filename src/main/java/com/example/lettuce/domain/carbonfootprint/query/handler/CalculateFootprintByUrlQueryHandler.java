package com.example.lettuce.domain.carbonfootprint.query.handler;

import org.springframework.cache.annotation.Cacheable;

import com.example.lettuce.api.carbonfootprint.dto.response.CarbonFootprintProductResponse;
import com.example.lettuce.domain.carbonfootprint.aggregate.CarbonFootPrintProduct;
import com.example.lettuce.domain.carbonfootprint.query.dto.CalculateFootprintByUrlQuery;
import com.example.lettuce.domain.carbonfootprint.repository.CarbonFootprintProductRepository;
import com.example.lettuce.global.framework.cqrs.QueryHandler;
import com.example.lettuce.global.shared.async.AsyncEventProducer;

import java.util.Collections;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CalculateFootprintByUrlQueryHandler
        implements QueryHandler<CalculateFootprintByUrlQuery, CarbonFootprintProductResponse> {
    private final CarbonFootprintProductRepository carbonFootprintProductRepository;
    private final AsyncEventProducer<CarbonFootPrintProduct> asyncEventProducer;

    /**
     * Calculate Carbon Footprint by URL.
     * <p>
     * This method retrieves a CarbonFootPrintProduct by URL from the crawler
     * and maps it to a CarbonFootprintProductResponse.
     * </p>
     *
     * @param url the URL of the product
     * @return a CarbonFootprintProductResponse containing product details and
     *         carbon footprint
     */
    @Cacheable(value = "carbon_footprint_product_by_product_id", key = "#query.url.split('/')[4]")
    @Override
    public CarbonFootprintProductResponse handle(CalculateFootprintByUrlQuery query) {
        CarbonFootprintProductResponse response = carbonFootprintProductRepository.findByUrl(query.url());

        asyncEventProducer
                .produce(Collections.singletonList(CarbonFootPrintProduct.of(query.user().getId(), response)));
        return response;
    }

}
