package com.example.demo.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Set;

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

    public Set<String> getDueTaskBatch(Instant now, long batch) {
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();
        return ops.rangeByScore(DELAY_QUEUE_KEY, 0D, now.toEpochMilli(), 0L, batch);
    }

    public void removeTask(String taskId) {
        redisTemplate.opsForZSet().remove(DELAY_QUEUE_KEY, taskId);
    }

}
