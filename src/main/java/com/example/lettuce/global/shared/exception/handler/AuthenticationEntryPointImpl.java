package com.example.lettuce.global.shared.exception.handler;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.lettuce.global.shared.response.CommonResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        log.error("Authentication failed: {}", authException.getMessage());
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(getErrorResponseEntity(authException));
    }

    private String getErrorResponseEntity(AuthenticationException exception) throws JsonProcessingException {
        return objectMapper.writeValueAsString(CommonResponse.fail(exception).getBody());
    }

}
