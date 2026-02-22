package com.example.demo.service;

import com.example.demo.model.event.CreateTaskEvent;
import com.example.demo.repository.TaskRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class CreateTaskEventListener {

    private final TaskRedisRepository taskRedisRepository;

    public CreateTaskEventListener(TaskRedisRepository taskRedisRepository) {
        this.taskRedisRepository = taskRedisRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreateTaskEvent(CreateTaskEvent event) {
        taskRedisRepository.schedule(event.taskId(), event.executeAt());
        log.info("task {} is scheduled at {}", event.taskId(), event.executeAt());
    }
}
