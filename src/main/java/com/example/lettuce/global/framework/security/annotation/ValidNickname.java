package com.example.lettuce.global.framework.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.lettuce.global.framework.security.validator.NicknameValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = NicknameValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNickname {
    String message() default "닉네임은 2자 이상 20자 이하여야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
