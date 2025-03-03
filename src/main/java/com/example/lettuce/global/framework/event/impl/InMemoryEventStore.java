package com.example.lettuce.global.framework.event.impl;

import com.example.lettuce.global.framework.event.DomainEvent;
import com.example.lettuce.global.framework.event.EventStore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryEventStore implements EventStore {
    private final Map<String, List<DomainEvent>> store = new ConcurrentHashMap<>();

    @Override
    public void save(DomainEvent event) {
        String aggregateId = event.getAggregateId();
        store.computeIfAbsent(aggregateId, k -> new ArrayList<>()).add(event);
    }

    @Override
    public List<DomainEvent> findByAggregateId(String aggregateId) {
        return store.getOrDefault(aggregateId, List.of());
    }

    @Override
    public void saveAll(Collection<DomainEvent> events) {
        events.forEach(this::save);
    }
}