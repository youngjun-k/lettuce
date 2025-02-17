package com.example.lettuce.global.shared.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.lettuce.global.framework.security.filter.JwtAuthenticationFilter;
import com.example.lettuce.global.framework.security.filter.JwtExceptionFilter;
import com.example.lettuce.global.framework.security.filter.RateLimitFilter;
import com.example.lettuce.global.shared.exception.handler.AccessDenialHandlerImpl;
import com.example.lettuce.global.shared.exception.handler.AuthenticationEntryPointImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

@Profile("!test")
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final AccessDenialHandlerImpl accessDenialHandler;
        private final AuthenticationEntryPointImpl authenticationEntryPoint;
        private final RateLimitFilter rateLimitFilter;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final JwtExceptionFilter jwtExceptionFilter;

        private static final String[] PERMIT_PATHS = {
                        "/api/auth/verify-email/**",
                        "/api/auth/login",
                        "/api/auth/register/**",
                        "/api/email/send/verify-email",
                        "/api/email/send/reset-password",
                        "/api/carbon-footprint/**",
        };

        private static final String[] ALLOW_ORIGINS = {
                        "http://localhost:3000",
        };

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .csrf(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .formLogin(AbstractHttpConfigurer::disable)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers(PERMIT_PATHS).permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                                .exceptionHandling(exception -> exception
                                                .accessDeniedHandler(accessDenialHandler)
                                                .authenticationEntryPoint(authenticationEntryPoint))

                                .build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration config = new CorsConfiguration();
                config.addAllowedHeader("*");
                config.addAllowedMethod("*");
                config.setAllowedOrigins(List.of(ALLOW_ORIGINS));
                config.addExposedHeader(HttpHeaders.AUTHORIZATION);
                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
        }

}