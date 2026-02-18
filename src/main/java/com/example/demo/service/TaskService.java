package com.example.demo.service;

import com.example.demo.entity.ScheduledTaskEntity;
import com.example.demo.model.request.CreateTaskRequest;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
    }

}
