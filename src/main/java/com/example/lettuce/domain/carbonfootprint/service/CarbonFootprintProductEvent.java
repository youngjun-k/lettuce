package com.example.lettuce.domain.carbonfootprint.service;

import org.springframework.context.ApplicationEvent;

import com.example.lettuce.domain.carbonfootprint.entity.CarbonFootPrintProduct;

import lombok.Getter;

@Getter
public class CarbonFootprintProductEvent extends ApplicationEvent {

    private final CarbonFootPrintProduct carbonFootprintProduct;

    public CarbonFootprintProductEvent(Object source, CarbonFootPrintProduct carbonFootprintProduct) {
        super(source);
        this.carbonFootprintProduct = carbonFootprintProduct;
    }
}
