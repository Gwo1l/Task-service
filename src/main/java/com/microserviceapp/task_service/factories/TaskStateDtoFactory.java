package com.microserviceapp.task_service.factories;

import com.microserviceapp.task_service.dto.TaskStateDto;
import com.microserviceapp.task_service.entities.TaskState;
import org.springframework.stereotype.Component;

@Component
public class TaskStateDtoFactory {
    public TaskStateDto makeTaskStateDto(TaskState taskState) {
        return TaskStateDto.builder()
                .id(taskState.getId())
                .name(taskState.getName())
                .createdAt(taskState.getCreatedAt())
                .build();
    }
}
