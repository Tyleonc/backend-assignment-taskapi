package com.example.demo.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RocketMQPublisher {

    private final DefaultMQProducer producer;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "task-schedule-topic";

    public RocketMQPublisher(DefaultMQProducer producer, ObjectMapper objectMapper) {
        this.producer = producer;
        this.objectMapper = objectMapper;
    }


    public SendResult publish(String taskId, ScheduleTaskMessage taskMessage) throws JsonProcessingException, MQBrokerException, RemotingException, InterruptedException, MQClientException {

        byte[] body = objectMapper.writeValueAsBytes(taskMessage);
        Message msg = new Message(TOPIC, "email", body);
        msg.setKeys(taskMessage.taskId());

        SendResult result = producer.send(msg, 2000);

        log.info("MQ published: taskId={}, msgId={}", taskId, result.getMsgId());
        return result;

    }

}
