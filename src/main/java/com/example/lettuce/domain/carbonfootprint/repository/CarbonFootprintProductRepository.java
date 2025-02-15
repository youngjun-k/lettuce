package com.example.lettuce.domain.carbonfootprint.repository;


import com.example.lettuce.domain.carbonfootprint.entity.CarbonFootPrintProduct;

public interface CarbonFootprintProductRepository {

    CarbonFootPrintProduct findByUrl(String url);

}
