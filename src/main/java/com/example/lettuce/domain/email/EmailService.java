package com.example.lettuce.domain.email;

import com.example.lettuce.api.email.dto.request.EmailResetPasswordRequest;
import com.example.lettuce.api.email.dto.request.EmailVerifyRequest;
import com.example.lettuce.domain.user.aggregate.User;
import com.example.lettuce.domain.user.event.UserRegistrationEvent;
import com.example.lettuce.domain.user.query.UserQueryService;
import com.example.lettuce.global.shared.constant.EmailConstants;
import com.example.lettuce.global.shared.exception.BaseException;
import com.example.lettuce.global.shared.exception.code.ErrorCode;
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
    private final UserQueryService userQueryService;
    private final String baseUrl;

    public EmailService(AsyncEmailSender asyncEmailSender, JwtTokenProvider jwtTokenProvider,
            UserQueryService userQueryService,
            @Value("${spring.application.client-base-url}") String baseUrl) {
        this.asyncEmailSender = asyncEmailSender;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userQueryService = userQueryService;
        this.baseUrl = baseUrl;
    }

    @EventListener
    private void sendWelcomeEmail(UserRegistrationEvent event) {
        this.sendEmail(event.getEmail(),
                EmailConstants.WELCOME_EMAIL_SUBJECT,
                EmailConstants.WELCOME_EMAIL_HTML,
                jwtTokenProvider.createEmailVerificationToken(event.getEmail()));
    }

    public void sendVerifyEmail(EmailVerifyRequest request) {
        User user = userQueryService.findByEmail(request.email());
        if (user.isVerified()) {
            throw new BaseException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }
        this.sendEmail(request.email(),
                EmailConstants.VERIFY_EMAIL_SUBJECT,
                EmailConstants.VERIFY_EMAIL_HTML,
                jwtTokenProvider.createEmailVerificationToken(request.email()));
    }

    public void sendResetPasswordEmail(EmailResetPasswordRequest request) {
        User user = userQueryService.findByEmail(request.email());
        if (!user.isVerified()) {
            throw new BaseException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
        this.sendEmail(request.email(),
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
