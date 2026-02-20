package com.example.demo.exception;

import java.time.Instant;

public record ErrorResponse(
        String error,
        Instant timestamp,
        int status,
        String message,
        String path
) {}
