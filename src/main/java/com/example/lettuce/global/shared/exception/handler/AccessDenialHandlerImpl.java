package com.example.lettuce.global.shared.exception.handler;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.lettuce.global.shared.response.CommonResponse;
import com.example.lettuce.global.shared.exception.code.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessDenialHandlerImpl implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        log.error("Authorization failed: {}", accessDeniedException.getMessage());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(getErrorResponseEntity(accessDeniedException));
    }

    private String getErrorResponseEntity(AccessDeniedException exception) throws JsonProcessingException {
        return objectMapper.writeValueAsString(CommonResponse.fail(ErrorCode.FORBIDDEN).getBody());
    }
}
