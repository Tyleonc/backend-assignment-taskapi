package com.example.demo.config;

import com.example.demo.service.TaskService;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public TaskService taskService() {
        return Mockito.mock(TaskService.class);
    }

    @Bean
    public DefaultMQProducer mockDefaultMQProducer() {
        return Mockito.mock(DefaultMQProducer.class);
    }

}
