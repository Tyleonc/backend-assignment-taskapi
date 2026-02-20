package com.example.demo.service;

import com.example.demo.entity.ScheduledTaskEntity;
import com.example.demo.exception.TaskExistsException;
import com.example.demo.model.TaskStatus;
import com.example.demo.model.request.CreateTaskRequest;
import com.example.demo.model.response.ListTaskResponse;
import com.example.demo.model.response.TaskResponse;
import com.example.demo.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Slf4j
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
        String taskId = request.taskId();
        if (taskRepository.existsByTaskId(taskId)) {
            log.warn("Create task rejected - duplicate taskId found: {}", taskId);
            throw new TaskExistsException(taskId);
        }

        ScheduledTaskEntity scheduledTask = new ScheduledTaskEntity();
        scheduledTask.setTaskId(taskId);
        scheduledTask.setExecuteAt(request.executeAt());
        scheduledTask.setCreatedAt(Instant.now());
        scheduledTask.setUpdatedAt(Instant.now());
        scheduledTask.setPayload(request.payload());

        try {
            taskRepository.save(scheduledTask);
        }catch (DataIntegrityViolationException e) {
            log.warn("Create task rejected - database constraint at taskId : {}", taskId);
            throw new TaskExistsException(taskId);
        }

        long score = request.executeAt().toEpochMilli();
        redisTemplate.opsForZSet().add(REDIS_TASK_KEY, taskId, score);
    }


    public TaskResponse getTask(String taskId) {
        //TODO: implement logic
        return new TaskResponse(taskId, null, null,null);
    }

    public void cancelTask(String taskId) {
        //TODO: implement logic
    }

    public ListTaskResponse listTasks(TaskStatus status, Pageable pageable) {
        //TODO: implement logic
        return null;
    }

}
