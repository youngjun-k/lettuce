package com.example.lettuce.global.infrastructure.event;

import com.lmax.disruptor.ExceptionHandler;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Custom exception handler for Disruptor events.
 * This handler properly logs exceptions and reports metrics when exceptions occur.
 */
@Slf4j
@Component
public class CustomDisruptorExceptionHandler implements ExceptionHandler<Object> {

    private final MeterRegistry meterRegistry;

    @Autowired
    public CustomDisruptorExceptionHandler(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
        log.error("Exception occurred while processing Disruptor event [sequence={}]: {}", 
                sequence, event, ex);
        
        // Increment error count metric
        meterRegistry.counter("disruptor.event.error", 
                "exception", ex.getClass().getSimpleName(),
                "event_type", event.getClass().getSimpleName())
                .increment();
        
        // Additional error handling logic can be added here
        // For example, we could publish a failure event to a different queue
        // or implement retry logic depending on the exception type
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        log.error("Exception occurred on Disruptor start: ", ex);
        meterRegistry.counter("disruptor.start.error").increment();
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        log.error("Exception occurred on Disruptor shutdown: ", ex);
        meterRegistry.counter("disruptor.shutdown.error").increment();
    }
} 