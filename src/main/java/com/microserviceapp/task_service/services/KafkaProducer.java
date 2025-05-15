package com.microserviceapp.task_service.services;

import gwoll.inc.TaskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducer {
    private final KafkaTemplate<String, TaskEvent> kafkaTemplate;

    public void sendEvent(TaskEvent event) {
        kafkaTemplate.send("task-events", event);
        log.info("Event with type={} sent to kafka", event.getEventType());
    }
}
