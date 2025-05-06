package com.microserviceapp.task_service.data.factories;

import com.microserviceapp.task_service.data.dto.TaskStateDto;
import com.microserviceapp.task_service.data.entities.TaskState;
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
