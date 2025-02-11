package com.example.lettuce.global.shared.config;

import java.util.List;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachingConfig {
    @Bean
    public CacheManager cacheManager() {

        /**
         * Consider using Caffeine cache instead of ConcurrentMapCache for better
         * control:
         * 
         * TTL for "login_attempts" cache to automatically clear old entries
         * Maximum size limits to prevent memory issues
         * Eviction policies for user-related caches
         */

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(
                List.of(
                        new ConcurrentMapCache("user_id"),
                        new ConcurrentMapCache("user_email")));
        return cacheManager;
    }
}
