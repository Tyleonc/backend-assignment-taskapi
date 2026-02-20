package com.example.demo.exception;


public class InvalidTaskStatusException extends RuntimeException {
    public InvalidTaskStatusException(String message) {
        super("Invalid status: " + message);
    }
}
