package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;

public record Payload(
        @NotBlank(message = "Payload type must be provided")
        String type,
        @NotBlank(message = "Payload target must be provided")
        String target,
        String message
) {}