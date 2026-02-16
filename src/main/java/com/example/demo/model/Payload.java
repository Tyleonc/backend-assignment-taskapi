package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;

public record Payload(
        @NotBlank
        String type,
        @NotBlank
        String target,
        @NotBlank
        String message
) {}