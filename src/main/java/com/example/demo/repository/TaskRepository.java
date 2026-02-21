package com.example.demo.repository;

import com.example.demo.entity.ScheduledTaskEntity;
import com.example.demo.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<ScheduledTaskEntity, Long> {

    boolean existsByTaskId(String taskId);

    Optional<ScheduledTaskEntity> findByTaskId(String taskId);

    Page<ScheduledTaskEntity> findByStatus(TaskStatus status, Pageable pageable);
}
