package com.example.lettuce.global.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for asynchronous Spring event handling.
 * Sets up a thread pool for processing events asynchronously.
 */
@Configuration
@Slf4j
public class AsynchronousSpringEventsConfig {

    /**
     * Creates and configures an ApplicationEventMulticaster bean for asynchronous
     * event handling.
     * 
     * @return Configured SimpleApplicationEventMulticaster with thread pool
     *         executor
     *         - Core pool size: 10 threads
     *         - Max pool size: 20 threads
     *         - Queue capacity: 50 tasks
     *         - Thread name prefix: AsyncEventExecutor-
     */
    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(50);
        taskExecutor.setThreadNamePrefix("AsyncEventExecutor-");
        taskExecutor.initialize();
        eventMulticaster.setTaskExecutor(taskExecutor);
        eventMulticaster.setErrorHandler(throwable -> {
            log.error("Exception occurred in event multicaster", throwable);
        });
        return eventMulticaster;
    }
}