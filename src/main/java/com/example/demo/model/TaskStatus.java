package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum TaskStatus {
    PENDING,
    PROCESSING,
    TRIGGERED,
    FAILED,
    CANCELLED;

    public static TaskStatus from(String value) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown status: " + value));
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
