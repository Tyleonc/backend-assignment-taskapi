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

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiException(ApiException ex, HttpServletRequest request) {
        ErrorCode code = ex.getCode();
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(code.getStatus(), ex.getMessage());
        detail.setTitle(code.getTitle());
        //TODO: extract path to application.yml
        detail.setType(URI.create("https://api.example.com/problems/" + code.getPath()));
        detail.setInstance(URI.create(request.getRequestURI()));
        return detail;
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ProblemDetail handleBadRequest(ConstraintViolationException ex, HttpServletRequest request) {
        //TODO: refactor to precise catch error from controller
        return createDetail(HttpStatus.BAD_REQUEST, "Invalid Request Parameter", ex.getMessage(), request);
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

    private ProblemDetail createDetail(HttpStatus status, String title, String message, HttpServletRequest request) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, message);
        detail.setTitle(title);
        detail.setType(URI.create("about:blank"));
        detail.setInstance(URI.create(request.getRequestURI()));
        detail.setProperty("timestamp", Instant.now());
        return detail;
    }

}
