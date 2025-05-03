package com.microserviceapp.task_service.factories;

import com.microserviceapp.task_service.dto.ProjectDto;
import com.microserviceapp.task_service.entities.Project;
import com.microserviceapp.task_service.entities.Task;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoFactory {
    public ProjectDto makeProjectDto(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
