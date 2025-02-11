package com.example.lettuce.global.framework.security.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

import com.example.lettuce.global.framework.security.annotation.Age;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {

    private int minAge;

    @Override
    public void initialize(Age age) {
        this.minAge = age.min();
    }

    @Override
    public boolean isValid(LocalDate birthday, ConstraintValidatorContext context) {
        if (birthday == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("생년월일을 입력해주세요.")
                    .addConstraintViolation();
            return false;
        }

        LocalDate today = LocalDate.now();
        int age = Period.between(birthday, today).getYears();

        if (birthday.isAfter(today)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("생년월일은 현재 날짜 이전이어야 합니다.")
                    .addConstraintViolation();
            return false;
        }

        return age >= minAge;
    }
}
