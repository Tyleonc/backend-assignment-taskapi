package com.example.demo.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({TaskExistsException.class, TaskUncancelableException.class})
    public ProblemDetail handleConflict(Exception ex, HttpServletRequest request) {
        String title = (ex instanceof TaskExistsException) ? "Duplicate Task ID" : "Task State Conflict";
        return createDetail(HttpStatus.CONFLICT, title, ex.getMessage(), request);
    }

    @ExceptionHandler({InvalidTaskStatusException.class, ConstraintViolationException.class})
    public ProblemDetail handleBadRequest(Exception ex, HttpServletRequest request) {
        String title = (ex instanceof InvalidTaskStatusException) ? "Invalid Task Status" : "Invalid Request Parameter";
        return createDetail(HttpStatus.BAD_REQUEST, title, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail detail = createDetail(HttpStatus.BAD_REQUEST, "Validation Failed", "Request validation failed", request);

        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        err -> err.getDefaultMessage() != null ? err.getDefaultMessage() : "Invalid value"));

        detail.setProperty("errors", errors);
        return detail;
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ProblemDetail handleTaskNotFound(TaskNotFoundException ex, HttpServletRequest request) {
        return createDetail(HttpStatus.NOT_FOUND, "Task Not Found", ex.getMessage(), request);
    }

    private ProblemDetail createDetail(HttpStatus status, String title, String message, HttpServletRequest request) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, message);
        detail.setTitle(title);
        detail.setType(URI.create("about:blank"));
        detail.setInstance(URI.create(request.getRequestURI()));
        detail.setProperty("timestamp", Instant.now());
        return detail;
    }

}
