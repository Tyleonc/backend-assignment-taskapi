package com.example.demo.worker;

import com.example.demo.mq.RocketMQPublisher;
import com.example.demo.repository.ClaimedTask;
import com.example.demo.repository.TaskDao;
import com.example.demo.repository.TaskRedisRepository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
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

        List<String> successTaskIds = new ArrayList<>();
        List<String> retryTaskIds = new ArrayList<>();
        for (ClaimedTask task : claimedTaskList) {
            try {
                //TODO: use custom message model
                SendResult result = mqPublisher.publish(task.taskId(), task.payload());
                if (SendStatus.SEND_OK == result.getSendStatus()) {
                    successTaskIds.add(task.taskId());
                }else {
                    retryTaskIds.add(task.taskId());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                retryTaskIds.add(task.taskId());
            }
        }

        if (CollectionUtils.isNotEmpty(successTaskIds)) {
            //TODO: introduce new status to differentiate from processing for retry
        }


        if (CollectionUtils.isNotEmpty(successTaskIds)) {
            taskDao.markTriggered(successTaskIds, appId);
        }

    }


}
