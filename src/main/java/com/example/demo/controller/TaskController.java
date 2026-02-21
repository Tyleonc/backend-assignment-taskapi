package com.example.demo.controller;

import com.example.demo.model.TaskStatus;
import com.example.demo.model.request.CreateTaskRequest;
import com.example.demo.model.response.ListTaskResponse;
import com.example.demo.model.response.TaskResponse;
import com.example.demo.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/tasks")
@Validated
public class TaskController {

    private final TaskService taskService;

    public TaskController (TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createTask(@Valid @RequestBody CreateTaskRequest request) {
        taskService.createTask(request);
        return ResponseEntity.created(URI.create("/tasks/" + request.taskId())).build();
    }


    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable String taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> cancelTask(@PathVariable String taskId) {
        taskService.cancelTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    public ResponseEntity<ListTaskResponse> getTaskList(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Invalid page value: must be >= 1") int page,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "Invalid size value: must be >= 1 ")
            @Max(value = 100, message = "Invalid size value: must be <= 100") int size) {
        return ResponseEntity.ok(taskService.listTasks(status, PageRequest.of(page - 1, size)));
    }

}