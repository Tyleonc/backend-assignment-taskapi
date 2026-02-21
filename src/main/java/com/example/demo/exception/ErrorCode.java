package com.example.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_STATUS(HttpStatus.BAD_REQUEST, "Invalid Task Status", "path1"),
    ALREADY_EXISTS(HttpStatus.CONFLICT, "Duplicate Task ID", "path2"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Task Not Found", "path3"),
    UNCANCELABLE(HttpStatus.CONFLICT, "Task State Conflict", "path4")
    ;

    @Getter
    private final HttpStatus status;
    @Getter
    private final String title;
    @Getter
    private final String path;

    ErrorCode(HttpStatus status, String title, String path) {
        this.status = status;
        this.title = title;
        this.path = path;
    }

}
