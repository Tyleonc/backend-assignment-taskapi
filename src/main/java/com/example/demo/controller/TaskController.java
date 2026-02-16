package com.example.demo.controller;

import com.example.demo.model.request.CreateTaskRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createTask(@Valid @RequestBody CreateTaskRequest request) {
        //TODO: add test
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}