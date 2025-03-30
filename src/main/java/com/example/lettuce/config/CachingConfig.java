package com.example.lettuce.config;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.extern.slf4j.Slf4j;

/**
 * Optimized caching configuration with a hybrid approach:
 * - Caffeine for local, in-memory caching (L1 cache)
 * - Redis for distributed caching (L2 cache)
 * 
 * This two-level caching strategy provides fast local access with the
 * benefit of distributed caching for cross-instance consistency.
 */
@Slf4j
@Configuration
@EnableCaching
public class CachingConfig {

    @Value("${spring.cache.redis.time-to-live:3600000}")
    private long redisTtlMillis;
    
    @Value("${spring.cache.redis.key-prefix:lettuce-cache}")
    private String cacheKeyPrefix;
    
    // Cache names for the application
    public static final String CACHE_USER_BY_EMAIL = "user_email";
    public static final String CACHE_USER_PROFILES = "profile";
    public static final String CACHE_CARBON_FOOTPRINT_BY_PRODUCT_ID = "carbon_footprint_product_by_product_id";
    public static final String CACHE_CARBON_FOOTPRINT_BY_PRODUCT_NAME = "carbon_footprint_product_by_product_name";
    public static final String CACHE_USER_EMAIL_WITH_PROFILES = "user_email_with_profiles";
    
    // List of caches that should be cached locally with Caffeine
    private final List<String> localCaches = Arrays.asList(
            CACHE_USER_BY_EMAIL,
            CACHE_CARBON_FOOTPRINT_BY_PRODUCT_ID
    );
    
    /**
     * Creates a composite cache manager that combines local Caffeine caching with Redis caching.
     * This provides a two-level caching strategy for optimal performance.
     */
    @Bean
    @Primary
    public CacheManager cacheManager(
            CacheManager caffeineCacheManager,
            CacheManager redisCacheManager) {
        
        CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
        compositeCacheManager.setCacheManagers(Arrays.asList(
                caffeineCacheManager,
                redisCacheManager
        ));
        compositeCacheManager.setFallbackToNoOpCache(false);
        
        log.info("Configured composite cache manager with Caffeine and Redis cache managers");
        return compositeCacheManager;
    }
    
    /**
     * Configures the local Caffeine cache manager for frequently accessed data.
     */
    @Bean
    public CacheManager caffeineCacheManager() {
        List<CaffeineCache> caches = localCaches.stream()
                .map(cacheName -> new CaffeineCache(cacheName, 
                        Caffeine.newBuilder()
                                .recordStats()
                                .expireAfterWrite(Duration.ofMinutes(10))
                                .maximumSize(1000)
                                .build()))
                .collect(Collectors.toList());
        
        org.springframework.cache.support.SimpleCacheManager cacheManager = new org.springframework.cache.support.SimpleCacheManager();
        cacheManager.setCaches(caches);
        
        log.info("Configured Caffeine cache manager with caches: {}", localCaches);
        return cacheManager;
    }
    
    /**
     * Configures the Redis cache manager for distributed caching.
     */
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMillis(redisTtlMillis))
                .disableCachingNullValues()
                .computePrefixWith(cacheName -> cacheKeyPrefix + ":" + cacheName + ":")
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        
        log.info("Configured Redis cache manager with TTL: {}ms", redisTtlMillis);
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .withCacheConfiguration(CACHE_USER_BY_EMAIL, 
                        cacheConfiguration.entryTtl(Duration.ofHours(1)))
                .withCacheConfiguration(CACHE_USER_PROFILES, 
                        cacheConfiguration.entryTtl(Duration.ofMinutes(30)))
                .withCacheConfiguration(CACHE_CARBON_FOOTPRINT_BY_PRODUCT_ID, 
                        cacheConfiguration.entryTtl(Duration.ofHours(4)))
                .withCacheConfiguration(CACHE_CARBON_FOOTPRINT_BY_PRODUCT_NAME, 
                        cacheConfiguration.entryTtl(Duration.ofHours(4)))
                .withCacheConfiguration(CACHE_USER_EMAIL_WITH_PROFILES, 
                        cacheConfiguration.entryTtl(Duration.ofMinutes(30)))
                .build();
    }
}
