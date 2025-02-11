package com.example.lettuce.global.framework.security.filter;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private static final String RATE_LIMIT_PREFIX = "rate_limit:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final DefaultRedisScript<Boolean> rateLimitScript;

    public RateLimitFilter(@Qualifier("rateLimitRedisTemplate") RedisTemplate<String, Object> redisTemplate,
            DefaultRedisScript<Boolean> rateLimitScript) {
        this.redisTemplate = redisTemplate;
        this.rateLimitScript = rateLimitScript;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String clientId = getClientIdentifier(request);
        String endpoint = request.getRequestURI();
        String rateLimitKey = RATE_LIMIT_PREFIX + clientId + ":" + endpoint;

        long currentTimestamp = Instant.now().getEpochSecond();
        long amount = 1; // 1 request per call
        long maxRequests = 100; // Max 100 requests
        long periodSeconds = 60; // 60-second window

        List<String> keys = Collections.singletonList(rateLimitKey);

        Boolean allowed = redisTemplate.execute(
                rateLimitScript,
                keys,
                currentTimestamp,
                amount,
                maxRequests,
                periodSeconds);

        if (allowed == null || !allowed) {
            handleRateLimitExceeded(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIdentifier(HttpServletRequest request) {
        // Implement your client identification logic
        return request.getRemoteAddr(); // Using IP address as example
    }

    private void handleRateLimitExceeded(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"error\": \"Rate limit exceeded\", \"code\": 429}");
    }

}