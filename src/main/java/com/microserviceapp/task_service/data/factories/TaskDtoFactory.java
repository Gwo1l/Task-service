package com.microserviceapp.task_service.data.factories;

import com.microserviceapp.task_service.data.dto.TaskDto;
import com.microserviceapp.task_service.data.entities.Task;
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
