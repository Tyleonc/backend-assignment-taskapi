package com.example.demo.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQProperties {

    private String namesrvAddr;

    private final ProducerProperties producer = new ProducerProperties();

    @Getter
    @Setter
    public static class ProducerProperties {
        private String group;
        private String topic;
    }

}
