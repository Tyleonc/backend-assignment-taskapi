package com.example.demo.config;

import com.example.demo.service.TaskService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WebMvcTestConfig {

    @Bean
    public TaskService taskService() {
        return Mockito.mock(TaskService.class);
    }
}
