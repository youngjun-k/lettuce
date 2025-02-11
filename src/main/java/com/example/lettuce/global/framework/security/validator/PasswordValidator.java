package com.example.lettuce.global.framework.security.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String LETTERS = "(?=.*[a-zA-Z])";
    private static final String DIGITS = "(?=.*\\d)";
    private static final String SPECIAL = "(?=.*[!@#$%^&*()_+\\-=~`\\[\\]{};':\",./<>?\\\\|])";
    private static final String LENGTH = ".{8,20}";
    private static final String PASSWORD_REGEX = "^" + LETTERS + DIGITS + SPECIAL + LENGTH + "$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return false;
        }
        return value.matches(PASSWORD_REGEX);
    }
}