package com.example.demo.worker;

import com.example.demo.entity.ScheduledTaskEntity;
import com.example.demo.repository.TaskRedisRepository;
import com.example.demo.repository.TaskRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
public class TaskRescheduler {

    private static final int FIXED_DELAY_INTERVAL = 30000;
    private static final Duration LOOKAHEAD = Duration.ofMinutes(10);
    private static final int RESCHEDULE_BATCH = 10;

    private final TaskRepository taskRepository;
    private final TaskRedisRepository taskRedisRepository;


    public TaskRescheduler(TaskRepository taskRepository, TaskRedisRepository taskRedisRepository) {
        this.taskRepository = taskRepository;
        this.taskRedisRepository = taskRedisRepository;
    }

    @Scheduled(fixedDelay = FIXED_DELAY_INTERVAL)
    public void reschedule() {

        //TODO: complete logic of reschedule

//        Instant upperBound = Instant.now().plus(LOOKAHEAD);
//        List<ScheduledTaskEntity> tasks = taskRepository.findRescheduleTask(upperBound, PageRequest.of(0, RESCHEDULE_BATCH));
//
//        if (CollectionUtils.isEmpty(tasks)) {
//            return;
//        }
//
//        for (ScheduledTaskEntity task : tasks) {
//            taskRedisRepository.schedule(task.getTaskId(), task.getExecuteAt());
//        }
    }

}
