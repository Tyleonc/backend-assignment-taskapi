package com.example.demo.config;

import com.example.demo.config.property.RocketMQProperties;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
public class RocketMQConfig {

    @Bean(destroyMethod = "shutdown")
    public DefaultMQProducer defaultMQProducer(RocketMQProperties properties) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(properties.getProducer().getGroup());
        producer.setNamesrvAddr(properties.getNamesrvAddr());
        producer.start();
        return producer;
    }

}
