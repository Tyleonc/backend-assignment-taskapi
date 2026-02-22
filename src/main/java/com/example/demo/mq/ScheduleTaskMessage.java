package com.example.demo.mq;


import com.example.demo.model.Payload;

import java.time.Instant;

public record ScheduleTaskMessage(
        String taskId,
        Instant sentAt,
        String source,
        String version,
        Payload payload
) {}
