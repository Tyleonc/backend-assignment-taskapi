package com.example.demo.model.event;

import java.time.Instant;

public record TaskEvent(
        EventType type,
        String taskId,
        Instant executeAt
) {}
