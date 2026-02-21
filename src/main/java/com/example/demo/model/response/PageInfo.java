package com.example.demo.model.response;

public record PageInfo(
        int page,
        int size,
        long totalElement,
        int totalPages
) {}
