package com.example.demo.worker;

import com.example.demo.mq.RocketMQPublisher;
import com.example.demo.repository.ClaimedTask;
import com.example.demo.repository.TaskDao;
import com.example.demo.repository.TaskRedisRepository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class TaskPoller {

    private static final long REDIS_BATCH_SIZE = 100L;
    private static final int DB_BATCH_SIZE = 100;

    private final TaskRedisRepository taskRedisRepository;
    private final TaskDao taskDao;
    private final String appId;

    private final RocketMQPublisher mqPublisher;

    public TaskPoller(TaskRedisRepository taskRedisRepository, TaskDao taskDao, @Qualifier("appId") String appId,
                      RocketMQPublisher mqPublisher) {
        this.taskRedisRepository = taskRedisRepository;
        this.taskDao = taskDao;
        this.appId = appId;
        this.mqPublisher = mqPublisher;
    }


    @Scheduled(fixedDelay = 1000)
    public void schedule() {

        log.info("try finding task ");

        Set<String> dueTask = taskRedisRepository.getDueTaskBatch(Instant.now(), REDIS_BATCH_SIZE);
        if (CollectionUtils.isEmpty(dueTask)) {
            return;
        }
        log.info("{} due task found", dueTask.size());

        List<ClaimedTask> claimedTaskList = taskDao.claimTask(dueTask, appId, DB_BATCH_SIZE);
        if(CollectionUtils.isEmpty(claimedTaskList)) {
            return;
        }
        log.info("scheduler-{} claimed {} tasks", appId, claimedTaskList.size());

        String[] claimedTaskIds = claimedTaskList.stream().map(ClaimedTask::taskId).toArray(String[]::new);
        taskRedisRepository.removeClaimedTasks(claimedTaskIds);

        List<String> successIds = new ArrayList<>();
        for (ClaimedTask task : claimedTaskList) {
            try {
                //TODO: use custom message model
                mqPublisher.publish(task.taskId(), task.payload());
                successIds.add(task.taskId());
            } catch (Exception e) {
                log.error("Failed to send message, release claimed task {}", task.taskId());
                // TODO: release claim
//                taskDao.releaseClaim(task.taskId());
            }
        }

        if (CollectionUtils.isNotEmpty(successIds)) {
            taskDao.markTriggered(successIds, appId);
        }

    }


}
