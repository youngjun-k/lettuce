package com.example.lettuce.global.framework.event.impl;

import java.time.LocalDateTime;

import com.example.lettuce.global.shared.entity.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity that represents a domain event in the database.
 * This is used by the JpaEventStore to persist domain events.
 */
@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "event_store", indexes = {
        @Index(name = "idx_event_store_aggregate_id", columnList = "aggregate_id"),
        @Index(name = "idx_event_store_occurred_on", columnList = "occurred_on")
})
public class EventEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true, length = 36)
    private String eventId;

    @Column(name = "aggregate_id", nullable = false, length = 36)
    private String aggregateId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "occurred_on", nullable = false)
    private LocalDateTime occurredOn;

    @Lob
    @Column(name = "event_data", nullable = false, columnDefinition = "LONGTEXT")
    private String eventData;
} 