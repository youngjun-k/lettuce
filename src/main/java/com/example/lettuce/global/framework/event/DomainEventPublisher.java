package com.example.lettuce.global.framework.event;

import java.util.Collection;

public interface DomainEventPublisher {
    void publish(DomainEvent event);

    void publishAll(Collection<DomainEvent> events);
}
