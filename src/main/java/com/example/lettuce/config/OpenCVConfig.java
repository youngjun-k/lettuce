package com.example.lettuce.config;

import org.opencv.core.Core;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for OpenCV.
 * Ensures that the OpenCV native library is loaded when the application starts.
 */
@Slf4j
@Configuration
public class OpenCVConfig {

    /**
     * Initializes OpenCV by loading the native library.
     */
    @PostConstruct
    public void init() {
        try {
            // Load the OpenCV native library
            nu.pattern.OpenCV.loadLocally();
            log.info("OpenCV loaded successfully: {}", Core.VERSION);
        } catch (Exception e) {
            log.error("Failed to load OpenCV native library", e);
        }
    }
} 