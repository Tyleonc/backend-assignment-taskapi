package com.example.demo;

import com.example.demo.config.WebMvcTestConfig;
import com.example.demo.controller.TaskController;
import com.example.demo.model.Payload;
import com.example.demo.model.request.CreateTaskRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(WebMvcTestConfig.class)
@WebMvcTest(value = TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturn201WhenTaskCreated() throws Exception {

        CreateTaskRequest body = new CreateTaskRequest(
                "abc-123",
                Instant.now().plusSeconds(60),
                new Payload("email", "hello@example.com", "This is a scheduled task!"));

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

}