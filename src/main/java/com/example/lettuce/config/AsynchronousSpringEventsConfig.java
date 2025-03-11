package com.example.lettuce.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for asynchronous Spring event handling.
 * Sets up a thread pool for processing events asynchronously with metrics and proper error handling.
 */
@Configuration
@Slf4j
public class AsynchronousSpringEventsConfig {

    @Value("${spring.event.async.core-pool-size:10}")
    private int corePoolSize;
    
    @Value("${spring.event.async.max-pool-size:20}")
    private int maxPoolSize;
    
    @Value("${spring.event.async.queue-capacity:100}")
    private int queueCapacity;
    
    @Value("${spring.event.async.keep-alive-seconds:60}")
    private int keepAliveSeconds;

    @Value("${spring.event.async.allow-core-thread-timeout:true}")
    private boolean allowCoreThreadTimeout;

    /**
     * Creates and configures an ApplicationEventMulticaster bean for asynchronous
     * event handling with enhanced monitoring and error handling.
     */
    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster applicationEventMulticaster(MeterRegistry meterRegistry) {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        
        ThreadPoolTaskExecutor taskExecutor = createTaskExecutor(meterRegistry);
        eventMulticaster.setTaskExecutor(taskExecutor);
        
        // Set up error handling
        eventMulticaster.setErrorHandler(throwable -> {
            if (throwable instanceof TaskRejectedException) {
                log.error("Event task rejected due to thread pool exhaustion. Consider increasing thread pool capacity.", throwable);
            } else {
                log.error("Exception occurred during event processing", throwable);
            }
        });
        
        return eventMulticaster;
    }
    
    /**
     * Creates a configured ThreadPoolTaskExecutor for event processing.
     */
    private ThreadPoolTaskExecutor createTaskExecutor(MeterRegistry meterRegistry) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setThreadNamePrefix("AsyncEventExecutor-");
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        taskExecutor.setAllowCoreThreadTimeOut(allowCoreThreadTimeout);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(60);
        taskExecutor.initialize();

        // Register with Micrometer to collect metrics
        ExecutorServiceMetrics.monitor(
            meterRegistry, 
            taskExecutor.getThreadPoolExecutor(),
            "async-event-executor",
            "Thread pool for processing application events"
        );
        
        // Log settings at startup
        log.info("Configured async event executor with core={}, max={}, queue={}, keepAlive={}sec",
                corePoolSize, maxPoolSize, queueCapacity, keepAliveSeconds);
                
        return taskExecutor;
    }
}