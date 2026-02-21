package com.example.demo.exception;

import com.example.demo.model.TaskStatus;

public class TaskUncancelableException extends RuntimeException {
    public TaskUncancelableException(String taskId, TaskStatus status) {
        super(String.format("task cannot be cancelled (taskId=%s, status=%s)", taskId, status));
    }
}
