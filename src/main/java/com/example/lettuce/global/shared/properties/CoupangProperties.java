package com.example.lettuce.global.shared.properties;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Component
@ConfigurationProperties(prefix = "spring.crawler.coupang")
@Slf4j
public class CoupangProperties {
    private String baseUrl;

    private String baseProductUrl;

    private String listUrl;

    public String getBaseProductUrl(String productUrl) {
        try {
            String[] segments = new URI(productUrl).getPath().split("/");
            if (segments.length >= 4 && "vm".equals(segments[1]) && "products".equals(segments[2])) {
                return this.baseProductUrl + segments[3];
            }
            throw new URISyntaxException(productUrl, "Invalid Format of Product URL");
        } catch (URISyntaxException e) {
            log.error("Invalid URL: {}", e.getMessage());
        }
        return this.baseUrl + productUrl;
    }

    public String getListUrl(String productName, int page, int offset) {
        return String.format(this.listUrl, productName, page, offset);
    }
}
