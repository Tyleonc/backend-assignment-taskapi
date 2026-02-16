package com.example.demo.model.request;

import com.example.demo.model.Payload;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateTaskRequest(
        @NotBlank
        String taskId,

        @NotNull
        @Future
        Instant executeAt,

        @NotNull
        Payload payload
) {}