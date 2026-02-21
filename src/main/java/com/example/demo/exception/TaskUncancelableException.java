package com.example.demo.exception;

import com.example.demo.model.TaskStatus;

public class TaskUncancelableException extends ApiException {
    public TaskUncancelableException(String taskId, TaskStatus status) {
        super(ErrorCode.UNCANCELABLE, String.format("task cannot be cancelled (taskId=%s, status=%s)", taskId, status));
    }
}
