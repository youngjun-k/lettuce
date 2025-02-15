package com.example.lettuce.global.framework.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.lettuce.global.framework.security.provider.JwtTokenProvider;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null) {
            String token = jwtTokenProvider.resolveToken(header);
            if (jwtTokenProvider.isValidateToken(token)) {
                authenticate(token, path);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(String token, String path) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token, path);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}