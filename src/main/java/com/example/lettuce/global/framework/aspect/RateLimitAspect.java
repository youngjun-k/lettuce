package com.example.lettuce.global.framework.aspect;

import com.example.lettuce.global.framework.annotation.RateLimitType;
import com.example.lettuce.global.framework.annotation.RateLimited;
import com.example.lettuce.global.shared.parser.CustomSpringELParser;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;

import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RateLimitAspect {

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Around("@annotation(com.example.lettuce.global.framework.annotation.RateLimited)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        RateLimited rateLimited = getRateLimited(joinPoint);
        String key = resolveKey(joinPoint, rateLimited);

        if (rateLimited.type() != RateLimitType.USER && !tryConsume(key, rateLimited.type())) {
            throw new BaseException(ErrorCode.TOO_MANY_REQUESTS);
        }
        return joinPoint.proceed();
    }

    @AfterReturning("execution(* com.example.lettuce.domain.auth.controller.AuthController.login(..))")
    public void handleLoginSuccess(JoinPoint joinPoint) {
        RateLimited rateLimited = getRateLimited(joinPoint);
        String key = resolveKey(joinPoint, rateLimited);
        buckets.remove(key);
    }

    @AfterThrowing("execution(* com.example.lettuce.domain.auth.controller.AuthController.login(..))")
    public void handleLoginFailure(JoinPoint joinPoint) {
        RateLimited rateLimited = getRateLimited(joinPoint);
        String key = resolveKey(joinPoint, rateLimited);
        if (!tryConsume(key, rateLimited.type())) {
            throw new BaseException(ErrorCode.TOO_MANY_REQUESTS);
        }
    }

    private boolean tryConsume(String key, RateLimitType type) {
        Bucket bucket = buckets.computeIfAbsent(key, k -> newBucket(k, type));
        return bucket.tryConsume(1);
    }

    private Bucket newBucket(String key, RateLimitType type) {
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(type.getLimit())
                        .refillGreedy(type.getRefill(), Duration.ofSeconds(type.getDuration())))
                .build();
    }

    // Helper method to fetch the current method from the join point.
    private Method getMethod(JoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod();
    }

    // Helper method to obtain the RateLimited annotation from the method.
    private RateLimited getRateLimited(JoinPoint joinPoint) {
        return getMethod(joinPoint).getAnnotation(RateLimited.class);
    }

    // Helper method to resolve the dynamic key based on the method, its arguments,
    // and the annotation key.
    private String resolveKey(JoinPoint joinPoint, RateLimited rateLimited) {
        return CustomSpringELParser.getDynamicValue(getMethod(joinPoint), joinPoint.getArgs(), rateLimited.key());
    }
}
