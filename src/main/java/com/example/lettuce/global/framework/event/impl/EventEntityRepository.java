package com.example.lettuce.global.framework.event.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing EventEntity objects in the database.
 */
@Repository
public interface EventEntityRepository extends JpaRepository<EventEntity, Long> {

    /**
     * Finds all events for a specific aggregate, ordered by occurrence time.
     */
    List<EventEntity> findByAggregateIdOrderByOccurredOnAsc(String aggregateId);

    /**
     * Finds all events of a specific type, ordered by occurrence time.
     */
    List<EventEntity> findByEventTypeOrderByOccurredOnAsc(String eventType);

    /**
     * Finds all events for a specific aggregate and type, ordered by occurrence
     * time.
     */
    @Query("SELECT e FROM EventEntity e WHERE e.aggregateId = :aggregateId AND e.eventType = :eventType ORDER BY e.occurredOn ASC")
    List<EventEntity> findByAggregateIdAndEventType(
            @Param("aggregateId") String aggregateId,
            @Param("eventType") String eventType);
}