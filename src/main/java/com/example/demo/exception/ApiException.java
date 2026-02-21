package com.example.demo.exception;

import lombok.Getter;

public abstract class ApiException extends RuntimeException {

    @Getter
    private final ErrorCode code;

    protected ApiException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
