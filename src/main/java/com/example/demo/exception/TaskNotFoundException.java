package com.example.demo.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String taskId) {
        super(String.format("task not found (taskId: %s)", taskId));
    }
}
