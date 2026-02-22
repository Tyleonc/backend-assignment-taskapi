package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ApplicationConfig {

    @Bean(name = "appId")
    public String appId() {
        return "app-" + UUID.randomUUID().toString().substring(0, 8);
    }

}
