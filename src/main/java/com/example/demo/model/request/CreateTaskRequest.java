package com.example.demo.model.request;

import com.example.demo.model.Payload;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateTaskRequest(
        @NotBlank(message = "taskId must not be null or blank")
        String taskId,

        @NotNull
        @Future(message = "Schedule time must be future time")
        Instant executeAt,

        @NotNull(message = "Payload must be provided")
        Payload payload
) {}