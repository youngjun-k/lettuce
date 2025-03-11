package com.example.lettuce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Configuration for Jackson ObjectMapper.
 * This class configures the ObjectMapper to handle serialization issues.
 */
@Configuration
public class JacksonConfig {

    /**
     * Creates and configures an ObjectMapper bean.
     * 
     * @return the configured ObjectMapper
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Register modules
        objectMapper.registerModule(new JavaTimeModule());
        
        // Configure features
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Prevent failures on empty beans (like BCryptPasswordEncoder)
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        
        // Allow unknown properties in JSON
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        // Accept strings as numbers where appropriate
        objectMapper.enable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        
        // Handle lazy loading exceptions by ignoring null values
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        return objectMapper;
    }
} 