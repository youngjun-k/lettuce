package com.example.lettuce.global.framework.security.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
// @Deprecated
public @interface ValidPassword {
    String message() default "비밀번호는 최소 8글자, 최대 20글자, 영문, 숫자, 특수문자를 포함해야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
