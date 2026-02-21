package com.example.demo.exception;

public class InvalidTaskStatusException extends ApiException {
    public InvalidTaskStatusException(String source) {
        super(ErrorCode.INVALID_STATUS, "invalid status: " + source);
    }
}
