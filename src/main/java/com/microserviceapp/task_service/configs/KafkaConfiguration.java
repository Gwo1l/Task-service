package com.microserviceapp.task_service.configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {
    @Bean
    public NewTopic newTopic() {
        return new NewTopic("task-events", 1, (short) 1);
    }
}
