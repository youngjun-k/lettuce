package com.example.lettuce.config;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.lettuce.global.framework.event.DisruptorEventQueue;
import com.example.lettuce.global.framework.event.DomainEvent;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for Disruptor.
 * Sets up the Disruptor event queue for high-performance event processing.
 */
@Slf4j
@Configuration
public class DisruptorConfig {

    /**
     * Buffer size for the Disruptor ring buffer.
     * Larger sizes provide more capacity but consume more memory.
     * Must be a power of 2.
     */
    @Value("${app.disruptor.buffer-size:16384}")
    private int bufferSize;
    
    /**
     * Number of worker threads for the Disruptor.
     * Set to a value lower than available CPU cores to avoid resource starvation.
     * For typical applications, 2-4 is a good starting point.
     */
    @Value("${app.disruptor.worker-threads:2}")
    private int workerThreads;

    /**
     * Wait strategy for the Disruptor.
     * Options: 'yielding', 'sleeping', 'blocking', 'busy-spin'
     * Default is 'yielding' which is fast but CPU intensive.
     * 'blocking' is less CPU intensive but slightly slower.
     */
    @Value("${app.disruptor.wait-strategy:yielding}")
    private String waitStrategy;

    /**
     * Creates a thread factory for Disruptor workers.
     * 
     * @return The thread factory
     */
    @Bean
    public ThreadFactory disruptorThreadFactory() {
        return new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);
            
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "disruptor-worker-" + counter.getAndIncrement());
                thread.setDaemon(true);
                // Set lower priority to avoid starving other application threads
                thread.setPriority(Thread.NORM_PRIORITY - 1);
                return thread;
            }
        };
    }

    /**
     * Creates and configures the Disruptor event queue.
     * 
     * @param applicationEventPublisher Spring's event publisher
     * @param meterRegistry             Metrics registry
     * @return The configured DisruptorEventQueue
     */
    @Bean
    public DisruptorEventQueue eventQueue(
            ApplicationEventPublisher applicationEventPublisher,
            MeterRegistry meterRegistry) {

        // Create the event queue with dependencies
        DisruptorEventQueue eventQueue = new DisruptorEventQueue(applicationEventPublisher, meterRegistry);

        // Create and configure the disruptor
        Disruptor<DisruptorEventQueue.EventHolder> disruptor = new Disruptor<>(
                DisruptorEventQueue.EventHolder::new,
                bufferSize,
                disruptorThreadFactory(),
                ProducerType.MULTI,
                getWaitStrategy());

        // Create counter for exception handler
        Counter eventsFailedCounter = Counter.builder("events.failed.handler")
                .description("Events that failed during processing in the exception handler")
                .register(meterRegistry);

        // Set up a default exception handler before starting
        disruptor.setDefaultExceptionHandler(new DisruptorExceptionHandler(eventsFailedCounter));

        // Connect handlers
        disruptor.handleEventsWith(eventQueue::processEvent);

        // Start the disruptor and get the ring buffer
        RingBuffer<DisruptorEventQueue.EventHolder> ringBuffer = disruptor.start();

        // Initialize the event queue with the disruptor and ring buffer
        eventQueue.initialize(disruptor, ringBuffer);

        log.info("Disruptor configured with buffer size {} and {} worker threads", bufferSize, workerThreads);

        return eventQueue;
    }

    /**
     * Custom exception handler for the Disruptor.
     */
    @RequiredArgsConstructor
    private static class DisruptorExceptionHandler implements ExceptionHandler<DisruptorEventQueue.EventHolder> {
        private final Counter eventsFailedCounter;

        @Override
        public void handleEventException(Throwable ex, long sequence, DisruptorEventQueue.EventHolder event) {
            DomainEvent domainEvent = event.getEvent();
            String eventId = domainEvent != null ? domainEvent.getEventId() : "unknown";
            log.error("Exception processing event {}: {}", eventId, ex.getMessage(), ex);
            eventsFailedCounter.increment();
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            log.error("Exception during Disruptor startup: {}", ex.getMessage(), ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            log.error("Exception during Disruptor shutdown: {}", ex.getMessage(), ex);
        }
    }

    // Method to configure the wait strategy
    private WaitStrategy getWaitStrategy() {
        switch (waitStrategy.toLowerCase()) {
            case "sleeping":
                log.info("Using SleepingWaitStrategy for Disruptor");
                return new SleepingWaitStrategy();
            case "blocking":
                log.info("Using BlockingWaitStrategy for Disruptor");
                return new BlockingWaitStrategy();
            case "busy-spin":
                log.info("Using BusySpinWaitStrategy for Disruptor");
                return new BusySpinWaitStrategy();
            case "yielding":
            default:
                log.info("Using YieldingWaitStrategy for Disruptor");
                return new YieldingWaitStrategy();
        }
    }
}
