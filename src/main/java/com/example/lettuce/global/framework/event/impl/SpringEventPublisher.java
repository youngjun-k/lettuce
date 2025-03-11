package com.example.lettuce.global.framework.event.impl;

import com.example.lettuce.global.framework.event.DisruptorEventQueue;
import com.example.lettuce.global.framework.event.DomainEvent;
import com.example.lettuce.global.framework.event.DomainEventPublisher;
import com.example.lettuce.global.framework.event.EventStore;
import com.example.lettuce.global.framework.event.HighPerformanceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class SpringEventPublisher implements DomainEventPublisher {
    private final DisruptorEventQueue eventQueue;
    private final ApplicationEventPublisher eventPublisher;
    private final EventStore eventStore;

    @Override
    @Transactional
    public void publish(DomainEvent event) {
        eventStore.save(event);
        if (event instanceof HighPerformanceEvent) {
            eventQueue.publish(event);
        } else {
            eventPublisher.publishEvent(event);
        }
    }

    @Override
    @Transactional
    public void publishAll(Collection<DomainEvent> events) {
        eventStore.saveAll(events);
        events.forEach(eventQueue::publish);
    }
}