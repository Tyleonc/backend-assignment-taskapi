package com.example.demo.service;

import com.example.demo.entity.ScheduledTaskEntity;
import com.example.demo.exception.TaskExistsException;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.exception.TaskUncancelableException;
import com.example.demo.model.TaskStatus;
import com.example.demo.model.event.CreateTaskEvent;
import com.example.demo.model.request.CreateTaskRequest;
import com.example.demo.model.response.ListTaskResponse;
import com.example.demo.model.response.PageInfo;
import com.example.demo.model.response.TaskResponse;
import com.example.demo.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TaskService(TaskRepository taskRepository, ApplicationEventPublisher eventPublisher) {
        this.taskRepository = taskRepository;
        this.eventPublisher = eventPublisher;
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
        scheduledTask.setPayload(request.payload());

        try {
            taskRepository.save(scheduledTask);
        }catch (DataIntegrityViolationException e) {
            log.warn("Create task rejected - database constraint at taskId : {}", taskId);
            throw new TaskExistsException(taskId);
        }

        eventPublisher.publishEvent(new CreateTaskEvent(taskId, request.executeAt()));
    }

    @Transactional(readOnly = true)
    public TaskResponse getTask(String taskId) {
        return taskRepository.findByTaskId(taskId)
                .map(this::toTaskResponse)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    @Transactional
    public void cancelTask(String taskId) {
        ScheduledTaskEntity entity = taskRepository.findByTaskId(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));

        TaskStatus entityStatus = entity.getStatus();
        if (entityStatus == TaskStatus.CANCELLED) {
            return;
        }

        if (entityStatus != TaskStatus.PENDING) {
            throw new TaskUncancelableException(taskId, entityStatus);
        }

        entity.setStatus(TaskStatus.CANCELLED);
    }

    @Transactional(readOnly = true)
    public ListTaskResponse listTasks(TaskStatus status, Pageable pageable) {
        TaskStatus queryStatus = (status == null) ? TaskStatus.PENDING : status;
        Page<ScheduledTaskEntity> taskEntities = (queryStatus == TaskStatus.PENDING) ?
                taskRepository.findByStatusAndExecuteAtAfter(TaskStatus.PENDING, Instant.now(), pageable) :
                taskRepository.findByStatus(queryStatus, pageable);

        List<TaskResponse> data = taskEntities.getContent().stream()
                .map(this::toTaskResponse)
                .toList();

        PageInfo pageInfo = new PageInfo(
                taskEntities.getNumber() + 1,
                taskEntities.getSize(),
                taskEntities.getTotalElements(),
                taskEntities.getTotalPages()
        );

        return new ListTaskResponse(data, pageInfo);
    }

    private TaskResponse toTaskResponse(ScheduledTaskEntity entity) {
        return new TaskResponse(
                entity.getTaskId(),
                entity.getStatus(),
                entity.getPayload(),
                entity.getExecuteAt()
        );
    }

}
