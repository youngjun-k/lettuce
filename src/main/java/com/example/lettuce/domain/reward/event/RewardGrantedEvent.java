package com.example.lettuce.domain.reward.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.lettuce.global.framework.event.DomainEvent;

import lombok.Getter;

/**
 * Event that is published when a reward is granted to a user.
 * This follows the Event Sourcing pattern for maintaining a log of all reward
 * transactions.
 */
@Getter
public class RewardGrantedEvent implements DomainEvent {
    private final String eventId;
    private final String aggregateId;
    private final String userId;
    private final int points;
    private final BigDecimal carbonSaved;
    private final String source;
    private final LocalDateTime occurredOn;

    public RewardGrantedEvent(
            String eventId,
            String aggregateId,
            String userId,
            int points,
            BigDecimal carbonSaved,
            String source,
            LocalDateTime occurredOn) {
        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.userId = userId;
        this.points = points;
        this.carbonSaved = carbonSaved;
        this.source = source;
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