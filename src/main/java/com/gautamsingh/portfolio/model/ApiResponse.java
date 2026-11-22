package com.gautamsingh.portfolio.model;

import java.time.Instant;

/** Uniform response envelope returned by the portfolio API. */
public record ApiResponse(String status, String message, Instant timestamp) {

    public static ApiResponse success(String message) {
        return new ApiResponse("SUCCESS", message, Instant.now());
    }

    public static ApiResponse error(String message) {
        return new ApiResponse("ERROR", message, Instant.now());
    }
}
