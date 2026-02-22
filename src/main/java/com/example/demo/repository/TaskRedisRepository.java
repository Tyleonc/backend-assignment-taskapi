package com.example.demo.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class TaskRedisRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String DELAY_QUEUE_KEY = "task:schedule";

    public TaskRedisRepository (StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void schedule(String taskId, Instant executeAt) {
        redisTemplate.opsForZSet().add(DELAY_QUEUE_KEY, taskId, executeAt.toEpochMilli());
    }

}
