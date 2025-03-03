package com.example.lettuce.domain.carbonfootprint.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.lettuce.global.framework.event.DomainEvent;

import lombok.Getter;

/**
 * Event that is published when a carbon footprint is calculated.
 * This follows the Event Sourcing pattern for maintaining a log of all calculations.
 */
@Getter
public class FootprintCalculatedEvent implements DomainEvent {
    private final String eventId;
    private final String aggregateId;
    private final String userId;
    private final BigDecimal carbonValue;
    private final BigDecimal carbonReduction;
    private final String productCategory;
    private final LocalDateTime occurredOn;

    public FootprintCalculatedEvent(
            String aggregateId,
            String userId,
            BigDecimal carbonValue,
            BigDecimal carbonReduction,
            String productCategory,
            LocalDateTime occurredOn) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.userId = userId;
        this.carbonValue = carbonValue;
        this.carbonReduction = carbonReduction;
        this.productCategory = productCategory;
        this.occurredOn = occurredOn;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }
} 