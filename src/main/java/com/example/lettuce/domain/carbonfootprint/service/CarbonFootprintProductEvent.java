package com.example.lettuce.domain.carbonfootprint.service;

import org.springframework.context.ApplicationEvent;

import com.example.lettuce.domain.carbonfootprint.dto.response.CarbonFootprintProductResponse;

import lombok.Getter;

@Getter
public class CarbonFootprintProductEvent extends ApplicationEvent {

    private final CarbonFootprintProductResponse carbonFootprintProduct;

    public CarbonFootprintProductEvent(Object source, CarbonFootprintProductResponse carbonFootprintProduct) {
        super(source);
        this.carbonFootprintProduct = carbonFootprintProduct;
    }
}
