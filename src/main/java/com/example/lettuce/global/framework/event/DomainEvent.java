package com.example.lettuce.global.framework.event;

import java.time.LocalDateTime;

public interface DomainEvent {
    String getEventId();

    LocalDateTime getOccurredOn();

    String getAggregateId();
}