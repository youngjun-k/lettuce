package com.example.lettuce.global.framework.security.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.lettuce.global.framework.security.validator.AgeValidator;

@Constraint(validatedBy = AgeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Age {

    String message() default "14세 이상만 가입이 가능합니다.";

    int min() default 14; // Minimum age

    int max() default Integer.MAX_VALUE; // Optional: Maximum age (if required)

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}