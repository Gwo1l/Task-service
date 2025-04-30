package com.microserviceapp.task_service.factories;

import com.microserviceapp.task_service.dto.TaskDto;
import com.microserviceapp.task_service.entities.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoFactory {
    public TaskDto makeTaskDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getName())
                .updatedAt(task.getUpdatedAt())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
