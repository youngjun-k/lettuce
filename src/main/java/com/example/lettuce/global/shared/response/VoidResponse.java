package com.example.lettuce.global.shared.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents an empty response body.
 * Used when an API endpoint succeeds but has no data to return.
 * This is a singleton record to ensure memory efficiency when representing
 * empty responses.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class VoidResponse {
    private static final VoidResponse INSTANCE = new VoidResponse();

    private VoidResponse() {}

    public static VoidResponse getInstance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "{}";
    }
}