package com.microserviceapp.task_service.data.factories;

import com.microserviceapp.task_service.data.dto.ProjectDto;
import com.microserviceapp.task_service.data.entities.Project;
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
