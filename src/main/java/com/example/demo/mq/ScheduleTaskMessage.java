package com.example.demo.mq;

import java.time.Instant;

public record ScheduleTaskMessage(
        String taskId,
        Instant sentAt,
        String source,
        String version,
        String payload
) {}
