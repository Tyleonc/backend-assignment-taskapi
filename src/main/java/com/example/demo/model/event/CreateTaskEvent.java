package com.example.demo.model.event;

import java.time.Instant;

public record CreateTaskEvent(
        String taskId,
        Instant executeAt
) {}
