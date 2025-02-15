package com.example.lettuce.domain.email.service;

import com.example.lettuce.domain.auth.service.UserRegistrationEvent;
import com.example.lettuce.domain.email.dto.request.EmailResetPasswordRequest;
import com.example.lettuce.domain.email.dto.request.EmailVerifyRequest;
import com.example.lettuce.domain.user.service.UserService;
import com.example.lettuce.global.shared.constant.EmailConstants;
import com.example.lettuce.global.framework.security.provider.JwtTokenProvider;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService {

    private final AsyncEmailSender asyncEmailSender;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final String baseUrl;

    public EmailService(AsyncEmailSender asyncEmailSender, JwtTokenProvider jwtTokenProvider, UserService userService,
            @Value("${lettuce.base-url}") String baseUrl) {
        this.asyncEmailSender = asyncEmailSender;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.baseUrl = baseUrl;
    }

    @EventListener
    private void sendWelcomeEmail(UserRegistrationEvent event) {
        sendEmail(event.getEmail(),
                EmailConstants.WELCOME_EMAIL_SUBJECT,
                EmailConstants.WELCOME_EMAIL_HTML,
                jwtTokenProvider.createEmailVerificationToken(event.getEmail()));
    }

    public void sendVerifyEmail(EmailVerifyRequest request) {
        userService.validateEmail(request.email());
        sendEmail(request.email(),
                EmailConstants.VERIFY_EMAIL_SUBJECT,
                EmailConstants.VERIFY_EMAIL_HTML,
                jwtTokenProvider.createEmailVerificationToken(request.email()));
    }

    public void sendResetPasswordEmail(EmailResetPasswordRequest request) {
        userService.validateEmail(request.email());
        sendEmail(request.email(),
                EmailConstants.RESET_PASSWORD_EMAIL_SUBJECT,
                EmailConstants.RESET_PASSWORD_EMAIL_HTML,
                jwtTokenProvider.createResetPasswordToken(request.email()));
    }

    private void sendEmail(String email, String subject, String template, String token) {
        Map<String, String> values = Map.of(
                "baseUrl", baseUrl,
                "encodedToken", token);
        String emailHtml = StringSubstitutor.replace(template, values);
        asyncEmailSender.sendEmail(email, subject, emailHtml);
    }
}
