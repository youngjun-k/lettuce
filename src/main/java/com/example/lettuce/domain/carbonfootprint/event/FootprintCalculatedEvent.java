package com.example.lettuce.domain.carbonfootprint.event;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEvent;

import com.example.lettuce.domain.carbonfootprint.aggregate.CarbonFootprint;
import com.example.lettuce.global.framework.event.DomainEvent;

import lombok.Getter;

/**
 * Event that is published when a carbon footprint is calculated.
 * This follows the Event Sourcing pattern for maintaining a log of all
 * calculations.
 */
@Getter
public class FootprintCalculatedEvent extends ApplicationEvent implements DomainEvent {
    private final CarbonFootprint carbonFootprint;

    public FootprintCalculatedEvent(CarbonFootprint carbonFootprint) {
        super(carbonFootprint);
        this.carbonFootprint = carbonFootprint;
    }

    @Override
    public String getEventId() {
        return carbonFootprint.getAggregateId();
    }

    @Override
    public String getAggregateId() {
        return carbonFootprint.getAggregateId();
    }

    @Override
    public LocalDateTime getOccurredOn() {
        return LocalDateTime.now();
    }
}