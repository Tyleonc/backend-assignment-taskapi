package com.example.demo.service;

import com.example.demo.entity.ScheduledTaskEntity;
import com.example.demo.model.request.CreateTaskRequest;
import com.example.demo.repository.TaskRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class TaskService {

    private static final String REDIS_TASK_KEY = "tasks:delayed";

    private final TaskRepository taskRepository;
    private final StringRedisTemplate redisTemplate;

    public TaskService(TaskRepository taskRepository, StringRedisTemplate redisTemplate) {
        this.taskRepository = taskRepository;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public void createTask(CreateTaskRequest request) {
        ScheduledTaskEntity scheduledTask = new ScheduledTaskEntity();
        scheduledTask.setTaskId(request.taskId());
        scheduledTask.setExecuteAt(request.executeAt());
        scheduledTask.setCreatedAt(Instant.now());
        scheduledTask.setUpdatedAt(Instant.now());
        scheduledTask.setPayload(request.payload());
        taskRepository.save(scheduledTask);

        long score = request.executeAt().toEpochMilli();
        redisTemplate.opsForZSet().add(REDIS_TASK_KEY, request.taskId(), score);
    }

}
