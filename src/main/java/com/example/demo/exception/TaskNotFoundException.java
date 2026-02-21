package com.example.demo.exception;

public class TaskNotFoundException extends ApiException {
    public TaskNotFoundException(String taskId) {
        super(ErrorCode.NOT_FOUND, String.format("task not found (taskId: %s)", taskId));
    }
}
