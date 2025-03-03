package com.example.lettuce.domain.user.aggregate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;

@Getter
public class Password {
    private String value;
    private final PasswordEncoder passwordEncoder;

    public void changePassword(String password) {
        this.value = password;
    }

    public boolean isMatch(String rawPassword) {
        return passwordEncoder.matches(rawPassword, this.value);
    }

    public void encodePassword(String password) {
        this.value = passwordEncoder.encode(password);
    }

    public Password(String encodedPassword) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.value = encodedPassword;
    }
}
