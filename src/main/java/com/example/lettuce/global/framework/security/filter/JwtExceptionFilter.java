package com.example.lettuce.global.framework.security.filter;

import org.springframework.stereotype.Component;

import com.example.lettuce.global.shared.response.CommonResponse;

import com.example.lettuce.global.shared.exception.AuthenticationException;

import com.example.lettuce.global.shared.exception.CustomJwtException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomJwtException ex) {
            sendJwtException(response, ex);
        } catch (AuthenticationException ex) {
            sendAuthenticationException(response, ex);
        }
    }

    private void sendJwtException(HttpServletResponse response, CustomJwtException exception)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(getErrorResponseEntity(exception));
    }

    private void sendAuthenticationException(HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(getErrorResponseEntity(exception));
    }

    private String getErrorResponseEntity(CustomJwtException exception) throws JsonProcessingException {
        return objectMapper.writeValueAsString(CommonResponse.fail(exception.getErrorCode()).getBody());
    }

    private String getErrorResponseEntity(AuthenticationException exception) throws JsonProcessingException {
        return objectMapper.writeValueAsString(CommonResponse.fail(exception.getErrorCode()).getBody());
    }
}