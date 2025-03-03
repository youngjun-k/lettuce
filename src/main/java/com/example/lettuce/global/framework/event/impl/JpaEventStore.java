package com.example.lettuce.global.framework.event.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.lettuce.global.framework.event.DomainEvent;
import com.example.lettuce.global.framework.event.EventStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JPA implementation of the EventStore interface.
 * This class is responsible for storing and retrieving domain events.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class JpaEventStore implements EventStore {

    private final EventEntityRepository eventEntityRepository;
    private final ObjectMapper objectMapper;

    /**
     * Saves a domain event to the event store.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(DomainEvent event) {
        try {
            EventEntity eventEntity = EventEntity.builder()
                    .eventId(event.getEventId())
                    .aggregateId(event.getAggregateId())
                    .eventType(event.getClass().getName())
                    .occurredOn(event.getOccurredOn())
                    .eventData(objectMapper.writeValueAsString(event))
                    .build();
            
            eventEntityRepository.save(eventEntity);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event: {}", event, e);
            throw new RuntimeException("Failed to serialize event", e);
        }
    }

    /**
     * Finds all domain events for a specific aggregate.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> findByAggregateId(String aggregateId) {
        List<EventEntity> eventEntities = eventEntityRepository.findByAggregateIdOrderByOccurredOnAsc(aggregateId);
        
        return eventEntities.stream()
                .map(this::deserializeEvent)
                .collect(Collectors.toList());
    }

    /**
     * Saves multiple domain events to the event store.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAll(Collection<DomainEvent> events) {
        List<EventEntity> eventEntities = events.stream()
                .map(event -> {
                    try {
                        return EventEntity.builder()
                                .eventId(event.getEventId())
                                .aggregateId(event.getAggregateId())
                                .eventType(event.getClass().getName())
                                .occurredOn(event.getOccurredOn())
                                .eventData(objectMapper.writeValueAsString(event))
                                .build();
                    } catch (JsonProcessingException e) {
                        log.error("Failed to serialize event: {}", event, e);
                        throw new RuntimeException("Failed to serialize event", e);
                    }
                })
                .collect(Collectors.toList());
        
        eventEntityRepository.saveAll(eventEntities);
    }
    
    /**
     * Deserializes an event entity to a domain event.
     */
    private DomainEvent deserializeEvent(EventEntity eventEntity) {
        try {
            Class<?> eventClass = Class.forName(eventEntity.getEventType());
            return (DomainEvent) objectMapper.readValue(eventEntity.getEventData(), eventClass);
        } catch (Exception e) {
            log.error("Failed to deserialize event: {}", eventEntity, e);
            throw new RuntimeException("Failed to deserialize event", e);
        }
    }
} 