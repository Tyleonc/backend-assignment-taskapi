package com.example.demo.worker;

import com.example.demo.mq.RocketMQPublisher;
import com.example.demo.repository.ClaimedTask;
import com.example.demo.repository.TaskDao;
import com.example.demo.repository.TaskRedisRepository;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class TaskScheduler {

    private static final long REDIS_BATCH_SIZE = 100L;
    private static final int DB_BATCH_SIZE = 100;

    private final TaskRedisRepository taskRedisRepository;
    private final TaskDao taskDao;
    private final String appId;

    private final RocketMQPublisher mqPublisher;

    public TaskScheduler(TaskRedisRepository taskRedisRepository, TaskDao taskDao, @Qualifier("appId") String appId,
                         RocketMQPublisher mqPublisher) {
        this.taskRedisRepository = taskRedisRepository;
        this.taskDao = taskDao;
        this.appId = appId;
        this.mqPublisher = mqPublisher;
    }


    @Scheduled(fixedDelay = 1000)
    public void schedule() {

        Set<String> dueTask = taskRedisRepository.getDueTaskBatch(Instant.now(), REDIS_BATCH_SIZE);
        if (CollectionUtils.isEmpty(dueTask)) {
            return;
        }

        List<ClaimedTask> claimedTaskList = taskDao.claimTask(dueTask, appId, DB_BATCH_SIZE);
        if(CollectionUtils.isEmpty(claimedTaskList)) {
            return;
        }

        List<String> successIds = new ArrayList<>();
        for (ClaimedTask task : claimedTaskList) {
            try {
                //TODO: use custom message model
                mqPublisher.publish(task.taskId(), task.payload());
                successIds.add(task.taskId());
            } catch (Exception e) {
                // TODO: release claim
//                taskDao.releaseClaim(task.taskId());
            }
        }

        if (CollectionUtils.isNotEmpty(successIds)) {
            taskDao.markTriggered(successIds, appId);
        }

    }


}
