package com.example.demo.model.response;

import com.example.demo.model.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record TaskResponse(
        @NotBlank
        String taskId,
        @NotNull
        String status,
        @NotNull
        Payload payload,
        Instant executeAt
) {}
