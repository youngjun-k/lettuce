package com.example.lettuce.global.framework.security.validator;

import com.example.lettuce.global.framework.security.annotation.ValidNickname;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NicknameValidator implements ConstraintValidator<ValidNickname, String> {

    private static final String NICKNAME_REGEX = "^[가-힣a-zA-Z0-9]{2,20}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.length() >= 2 && value.length() <= 20 && value.matches(NICKNAME_REGEX);
    }
}
