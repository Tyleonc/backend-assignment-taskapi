package com.example.demo;

import com.example.demo.config.WebMvcTestConfig;
import com.example.demo.controller.TaskController;
import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.exception.TaskExistsException;
import com.example.demo.model.Payload;
import com.example.demo.model.request.CreateTaskRequest;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.Instant;

import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({WebMvcTestConfig.class, GlobalExceptionHandler.class})
@WebMvcTest(value = TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskService taskService;

    @Test
    void createTask_shouldReturn201_whenValidRequest() throws Exception {
        CreateTaskRequest request = createRequest();
        doNothing().when(taskService).createTask(request);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createTask_shouldReturn409_whenTaskAlreadyExists() throws Exception {
        CreateTaskRequest request = createRequest();

        willThrow(new TaskExistsException("abc-123"))
                .given(taskService)
                .createTask(request);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.title").value("Duplicate Task ID"))
                .andExpect(jsonPath("$.detail").value("Task already exists: abc-123"))
                .andExpect(jsonPath("$.instance").value("/tasks"));
    }

    private CreateTaskRequest createRequest() {
        return new CreateTaskRequest(
                "abc-123",
                Instant.now().plusSeconds(3600),
                new Payload("email", "hello@example.com", "hi")
        );
    }

}