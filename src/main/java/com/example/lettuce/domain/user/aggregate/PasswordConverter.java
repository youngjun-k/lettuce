package com.example.lettuce.domain.user.aggregate;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PasswordConverter implements AttributeConverter<Password, String> {

    private final PasswordEncoder passwordEncoder;

    @Lazy
    public PasswordConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String convertToDatabaseColumn(Password attribute) {
        return passwordEncoder.encode(attribute.getValue());
    }

    @Override
    public Password convertToEntityAttribute(String dbData) {
        return new Password(dbData);
    }

}
