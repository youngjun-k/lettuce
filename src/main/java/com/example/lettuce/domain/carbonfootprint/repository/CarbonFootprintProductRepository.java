package com.example.lettuce.domain.carbonfootprint.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintProductResponse;

public interface CarbonFootprintProductRepository {

    CarbonFootprintProductResponse findByUrl(String url);

    Page<CarbonFootprintProductResponse> findByName(String name, Pageable pageable);

}
