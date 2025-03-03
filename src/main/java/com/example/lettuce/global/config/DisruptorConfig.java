package com.example.lettuce.global.config;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.lettuce.global.framework.event.DisruptorEventQueue;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for Disruptor.
 * Sets up the Disruptor event queue for high-performance event processing.
 */
@Slf4j
@Configuration
public class DisruptorConfig {

    @Value("${app.disruptor.buffer-size:16384}")
    private int bufferSize;
    
    @Value("${app.disruptor.worker-threads:4}")
    private int workerThreads;
    
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
                return thread;
            }
        };
    }
    
    @Bean
    public DisruptorEventQueue eventQueue() {
        // Create a temporary instance just for construction
        return new DisruptorEventQueue();
    }
    
    /**
     * Creates a Disruptor for the event queue.
     * 
     * @param threadFactory The thread factory to use
     * @param eventQueue The event queue to process events
     * @return The Disruptor
     */
    @Bean
    public Disruptor<DisruptorEventQueue.EventHolder> disruptor(
            ThreadFactory disruptorThreadFactory, 
            DisruptorEventQueue eventQueue) {
        Disruptor<DisruptorEventQueue.EventHolder> disruptor = new Disruptor<>(
                DisruptorEventQueue.EventHolder::new,
                bufferSize,
                disruptorThreadFactory);
        
        // Connect handlers
        disruptor.handleEventsWith(eventQueue::processEvent);
        
        log.info("Disruptor configured with buffer size {} and {} worker threads", bufferSize, workerThreads);
        
        return disruptor;
    }
    
    /**
     * Creates a RingBuffer from the Disruptor.
     * 
     * @param disruptor The Disruptor
     * @param eventQueue The event queue
     * @return The RingBuffer
     */
    @Bean
    public RingBuffer<DisruptorEventQueue.EventHolder> ringBuffer(
            Disruptor<DisruptorEventQueue.EventHolder> disruptor,
            DisruptorEventQueue eventQueue) {
        RingBuffer<DisruptorEventQueue.EventHolder> ringBuffer = disruptor.start();
        // Now that we have the real objects, initialize the event queue
        eventQueue.initialize(disruptor, ringBuffer);
        return ringBuffer;
    }
} 