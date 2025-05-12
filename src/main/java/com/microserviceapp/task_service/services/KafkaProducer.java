package com.microserviceapp.task_service.services;

import gwoll.inc.TaskEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class KafkaProducer {
    private final KafkaTemplate<String, TaskEvent> kafkaTemplate;

    public void sendEvent(TaskEvent event) {
        kafkaTemplate.send("task-events", event);
    }
}
