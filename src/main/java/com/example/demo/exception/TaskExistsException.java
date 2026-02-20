package com.example.demo.exception;

import lombok.Getter;

@Getter
public class TaskExistsException extends RuntimeException {

    private final String taskId;

    public TaskExistsException(String taskId) {
        super("Task already exists: " + taskId);
        this.taskId = taskId;
    }
}
