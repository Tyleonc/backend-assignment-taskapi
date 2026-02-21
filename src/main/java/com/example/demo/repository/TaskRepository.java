package com.example.demo.repository;

import com.example.demo.entity.ScheduledTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<ScheduledTaskEntity, Long> {

    boolean existsByTaskId(String taskId);

    Optional<ScheduledTaskEntity> findByTaskId(String taskId);
}
