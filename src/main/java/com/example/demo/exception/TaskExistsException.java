package com.example.demo.exception;

public class TaskExistsException extends ApiException {
    public TaskExistsException(String taskId) {
        super(ErrorCode.ALREADY_EXISTS, "task already exists: " + taskId);
    }
}
