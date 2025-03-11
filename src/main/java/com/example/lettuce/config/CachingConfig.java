package com.example.lettuce.config;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableCaching
@Slf4j
public class CachingConfig {

    // Cache TTLs in seconds
    private static final int DEFAULT_TTL = 3600; // 1 hour
    private static final int PROFILE_TTL = 1800; // 30 minutes
    private static final int PRODUCT_TTL = 86400; // 24 hours

    // Cache names
    public static final String USER_EMAIL_CACHE = "user_email";
    public static final String PROFILE_CACHE = "profile";
    public static final String CARBON_FOOTPRINT_BY_PRODUCT_ID_CACHE = "carbon_footprint_product_by_product_id";
    public static final String CARBON_FOOTPRINT_BY_PRODUCT_NAME_CACHE = "carbon_footprint_product_by_product_name";
    public static final String USER_EMAIL_WITH_PROFILES_CACHE = "user_email_with_profiles";

    // Local caches (Caffeine)
    private static final List<String> LOCAL_CACHES = Arrays.asList(
            USER_EMAIL_CACHE,
            PROFILE_CACHE);

    // Distributed caches (Redis)
    private static final List<String> DISTRIBUTED_CACHES = Arrays.asList(
            CARBON_FOOTPRINT_BY_PRODUCT_ID_CACHE,
            CARBON_FOOTPRINT_BY_PRODUCT_NAME_CACHE,
            USER_EMAIL_WITH_PROFILES_CACHE);

    /**
     * Primary cache manager using Caffeine for local caching.
     * Used for high-frequency, low-value caches that don't need to be shared.
     */
    @Primary
    @Bean
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofSeconds(DEFAULT_TTL))
                .recordStats());
        cacheManager.setCacheNames(LOCAL_CACHES);
        log.info("Configured Caffeine cache for local caching with caches: {}", LOCAL_CACHES);
        return cacheManager;
    }

    /**
     * Redis cache customizer for distributed caching.
     * Used for data that needs to be consistent across multiple instances.
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> {
            // Default cache configuration
            RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofSeconds(DEFAULT_TTL))
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                            new GenericJackson2JsonRedisSerializer()));

            // Cache-specific configurations
            builder
                    .withCacheConfiguration(CARBON_FOOTPRINT_BY_PRODUCT_ID_CACHE,
                            defaultConfig.entryTtl(Duration.ofSeconds(PRODUCT_TTL)))
                    .withCacheConfiguration(CARBON_FOOTPRINT_BY_PRODUCT_NAME_CACHE,
                            defaultConfig.entryTtl(Duration.ofSeconds(PRODUCT_TTL)))
                    .withCacheConfiguration(USER_EMAIL_WITH_PROFILES_CACHE,
                            defaultConfig.entryTtl(Duration.ofSeconds(PROFILE_TTL)));

            log.info("Configured Redis cache for distributed caching with caches: {}", DISTRIBUTED_CACHES);
        };
    }

    /**
     * Register cache statistics for monitoring.
     */
    @Bean(name = "customCacheMetricsRegistrar")
    public CacheMetricsRegistrar cacheMetricsRegistrar(CaffeineCacheManager caffeineCacheManager) {
        return new CacheMetricsRegistrar(caffeineCacheManager.getCacheNames());
    }

    /**
     * Helper class to register cache metrics with Micrometer.
     */
    public static class CacheMetricsRegistrar {
        public CacheMetricsRegistrar(Iterable<String> cacheNames) {
            // Register cache metrics with your metrics registry
            // In a real implementation, this would use Micrometer APIs
            log.info("Registered cache metrics for caches: {}", cacheNames);
        }
    }
}
