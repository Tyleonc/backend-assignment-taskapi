package com.example.demo.model.response;

import com.example.demo.model.Payload;
import com.example.demo.model.TaskStatus;

import java.time.Instant;

public record TaskResponse(
        String taskId,
        TaskStatus status,
        Payload payload,
        Instant executeAt
) {}
