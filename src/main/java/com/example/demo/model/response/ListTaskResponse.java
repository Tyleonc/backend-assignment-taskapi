package com.example.demo.model.response;

import java.util.List;

public record ListTaskResponse(
        List<TaskResponse> tasks,
        PageInfo pageInfo
) {}
