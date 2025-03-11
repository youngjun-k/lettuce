package com.example.lettuce.domain.user.event;

import java.time.LocalDateTime;

import java.util.UUID;

import com.example.lettuce.global.framework.event.DomainEvent;

public class UserRegistrationEvent implements DomainEvent {

    private final String eventId = UUID.randomUUID().toString();
    private final LocalDateTime occurredOn = LocalDateTime.now();
    private final String email;

    public UserRegistrationEvent(String email) {        
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public String getEventId() {
        return this.eventId;
    }

    @Override
    public LocalDateTime getOccurredOn() {
        return this.occurredOn;
    }

    @Override
    public String getAggregateId() {
        return this.email;
    }
}
