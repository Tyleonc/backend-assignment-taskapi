package com.example.demo.service;

import com.example.demo.model.event.TaskEvent;
import com.example.demo.repository.TaskRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;

@Component
@Slf4j
public class CreateTaskEventListener {

    private final TaskRedisRepository taskRedisRepository;

    public CreateTaskEventListener(TaskRedisRepository taskRedisRepository) {
        this.taskRedisRepository = taskRedisRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTaskEvent(TaskEvent event) {

        switch (event.type()) {
            case CREATE -> {
                taskRedisRepository.schedule(event.taskId(), event.executeAt());
                log.info("task {} is scheduled at {}", event.taskId(), event.executeAt());
            }

            case CANCEL -> {
                taskRedisRepository.removeSingleTask(event.taskId());
                log.info("task {} is cancelled at {}", event.taskId(), Instant.now());
            }
        }
    }
}
