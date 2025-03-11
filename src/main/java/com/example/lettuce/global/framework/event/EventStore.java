package com.example.lettuce.global.framework.event;

import java.util.Collection;
import java.util.List;

public interface EventStore {
    void save(DomainEvent event);

    List<DomainEvent> findByAggregateId(String aggregateId);

    void saveAll(Collection<DomainEvent> events);
}