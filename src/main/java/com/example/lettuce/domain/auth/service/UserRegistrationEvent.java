package com.example.lettuce.domain.auth.service;

import org.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {

    private final String email;

    public UserRegistrationEvent(String email) {
        super(email);
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
