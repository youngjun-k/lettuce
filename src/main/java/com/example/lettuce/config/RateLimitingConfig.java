package com.example.lettuce.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Configuration for API rate limiting using Redis storage.
 * This improves application performance by preventing excessive API usage
 * across multiple instances.
 */
@Slf4j
@Configuration
public class RateLimitingConfig implements WebMvcConfigurer {

    private static final String RATE_LIMIT_PREFIX = "rate-limit:";
    private static final String IP_PREFIX = "ip:";
    private static final String USER_PREFIX = "user:";
    private static final int AUTHENTICATED_LIMIT = 50;
    private static final int ANONYMOUS_LIMIT = 20;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    private final RedisTemplate<String, Object> redisTemplate;

    public RateLimitingConfig(CacheManager cacheManager, RedisTemplate<String, Object> rateLimitRedisTemplate) {
        this.redisTemplate = rateLimitRedisTemplate;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitingInterceptor()).addPathPatterns("/api/**");
        log.info("Registered rate limiting interceptor for /api/** paths");
    }

    @Bean
    public RateLimitingInterceptor rateLimitingInterceptor() {
        return new RateLimitingInterceptor(this::getRateLimiter);
    }

    /**
     * Get a rate limiter for a client, using Redis-based storage.
     * Default is 50 requests per minute for authenticated users
     * and 20 requests per minute for anonymous users.
     *
     * @param clientKey     The client identifier (IP or user ID)
     * @param authenticated Whether the request is authenticated
     * @return A rate limiter for this client
     */
    public RedisRateLimiter getRateLimiter(String clientKey, boolean authenticated) {
        int limit = authenticated ? AUTHENTICATED_LIMIT : ANONYMOUS_LIMIT;
        return new RedisRateLimiter(clientKey, limit, WINDOW, redisTemplate);
    }

    /**
     * Custom implementation of a rate limiter backed by Redis
     */
    @Slf4j
    public static class RedisRateLimiter {
        private static final String SCRIPT = "local current = redis.call('incr', KEYS[1])\n" +
                "if current == 1 then\n" +
                "    redis.call('expire', KEYS[1], ARGV[1])\n" +
                "end\n" +
                "return current <= tonumber(ARGV[2])";

        private final String key;
        private final int maxRequests;
        private final Duration window;
        private final RedisTemplate<String, Object> redisTemplate;
        private final RedisScript<Boolean> rateLimitScript;

        public RedisRateLimiter(String key, int maxRequests, Duration window,
                RedisTemplate<String, Object> redisTemplate) {
            this.key = RATE_LIMIT_PREFIX + key;
            this.maxRequests = maxRequests;
            this.window = window;
            this.redisTemplate = redisTemplate;

            DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptText(SCRIPT);
            redisScript.setResultType(Boolean.class);
            this.rateLimitScript = redisScript;
        }

        /**
         * Check if the request should be allowed based on rate limits
         * 
         * @return true if allowed, false if rate limit exceeded
         */
        public boolean allowRequest() {
            try {
                // Execute the Lua script atomically using RedisTemplate's execute
                Boolean allowed = redisTemplate.execute(
                        rateLimitScript,
                        Collections.singletonList(key),
                        window.getSeconds(), maxRequests);

                return allowed != null && allowed;
            } catch (Exception e) {
                // In case of Redis errors, allow the request to avoid blocking users
                log.error("Error checking rate limit for key: {}", key, e);
                return true;
            }
        }
    }

    /**
     * Inner class for the rate limiting interceptor.
     * This intercepts HTTP requests and applies rate limiting.
     */
    @Slf4j
    public static class RateLimitingInterceptor implements HandlerInterceptor {

        private final java.util.function.BiFunction<String, Boolean, RedisRateLimiter> rateLimiterResolver;

        public RateLimitingInterceptor(
                java.util.function.BiFunction<String, Boolean, RedisRateLimiter> rateLimiterResolver) {
            this.rateLimiterResolver = rateLimiterResolver;
        }

        @Override
        public boolean preHandle(HttpServletRequest request,
                HttpServletResponse response,
                Object handler) throws Exception {

            // Determine client key (IP address or user ID if authenticated)
            boolean authenticated = isAuthenticated(request);
            String clientKey = getClientKey(request, authenticated);

            // Get rate limiter for this client
            RedisRateLimiter rateLimiter = rateLimiterResolver.apply(clientKey, authenticated);

            // Check if request is allowed based on rate limits
            if (rateLimiter.allowRequest()) {
                return true;
            } else {
                // Rate limit exceeded
                response.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Rate limit exceeded. Please try again later.\"}");

                log.warn("Rate limit exceeded for client: {}", clientKey);
                return false;
            }
        }

        private String getClientKey(HttpServletRequest request, boolean authenticated) {
            if (authenticated) {
                Object principal = request.getUserPrincipal();
                return USER_PREFIX + principal.toString();
            }

            // Fall back to IP address
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                return IP_PREFIX + xForwardedFor.split(",")[0].trim();
            }

            return IP_PREFIX + request.getRemoteAddr();
        }

        private boolean isAuthenticated(HttpServletRequest request) {
            return request.getUserPrincipal() != null;
        }
    }
}