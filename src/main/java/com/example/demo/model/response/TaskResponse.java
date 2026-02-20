package com.example.demo.model.response;

import com.example.demo.model.Payload;

import java.time.Instant;

public record TaskResponse(
        String taskId,
        String status,
        Payload payload,
        Instant executeAt
) {}
