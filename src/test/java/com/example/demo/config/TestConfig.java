package com.example.demo.config;

import com.example.demo.repository.TaskRedisRepository;
import com.example.demo.service.TaskService;
import com.example.demo.worker.TaskPoller;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

@TestConfiguration
public class TestConfig {

    @Bean
    public TaskService mockTaskService() {
        return Mockito.mock(TaskService.class);
    }

    @Bean
    public DefaultMQProducer mockDefaultMQProducer() {
        return Mockito.mock(DefaultMQProducer.class);
    }

    @Bean
    public StringRedisTemplate mockRedisTemplate() {
        return Mockito.mock(StringRedisTemplate.class);
    }

    @Bean
    public TaskRedisRepository mockTaskRedisRepository() {
        return Mockito.mock(TaskRedisRepository.class);
    }

    @Bean
    public TaskPoller mockTaskPoller() {
        return Mockito.mock(TaskPoller.class);
    }

}
