package com.example.lettuce.domain.carbonfootprint.service;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class CarbonFootprintProductEvent extends ApplicationEvent {

    private final List<CarbonFootPrintProduct> carbonFootprintProducts;

    public CarbonFootprintProductEvent(Object source, List<CarbonFootPrintProduct> carbonFootprintProducts) {
        super(source);
        this.carbonFootprintProducts = carbonFootprintProducts;
    }
}
