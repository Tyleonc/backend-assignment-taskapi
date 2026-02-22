package com.example.demo.config;

import com.example.demo.config.property.RocketMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
@Slf4j
public class RocketMQConfig {

    @Bean(destroyMethod = "shutdown")
    public DefaultMQProducer defaultMQProducer(RocketMQProperties properties) throws MQClientException {
        String group = Optional.ofNullable(properties.getProducer())
                .map(RocketMQProperties.ProducerProperties::getGroup)
                .orElse(null);

        String nameSrvAddr = properties.getNamesrvAddr();
        DefaultMQProducer producer = new DefaultMQProducer(group);
        producer.setNamesrvAddr(nameSrvAddr);
        producer.start();

        log.info("RocketMQ Producer initialized: group={}, instance={}, namesrv={}",
                group, producer.getInstanceName(), nameSrvAddr);

        return producer;
    }

}
