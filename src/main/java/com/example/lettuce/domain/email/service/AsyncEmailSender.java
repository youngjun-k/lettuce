package com.example.lettuce.domain.email.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.lettuce.global.framework.annotation.RateLimitType;
import com.example.lettuce.global.framework.annotation.RateLimited;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsyncEmailSender {

    private final JavaMailSender mailSender;

    @Value("classpath:static/images/logo.png")
    private Resource logoImage;

    @Value("classpath:static/images/character.png")
    private Resource characterImage;

    @RateLimited(key = "#p0", type = RateLimitType.EMAIL)
    @Async("asyncExecutor")
    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.addInline("logo.png", logoImage);
            helper.addInline("character.png", characterImage);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}