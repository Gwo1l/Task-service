package com.microserviceapp.task_service.services;

import com.microserviceapp.task_service.data.dto.ProjectDto;
import com.microserviceapp.task_service.data.entities.Project;
import com.microserviceapp.task_service.exceptions.BadRequestException;
import com.microserviceapp.task_service.exceptions.NotFoundException;
import com.microserviceapp.task_service.data.factories.ProjectDtoFactory;
import com.microserviceapp.task_service.data.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectDtoFactory projectDtoFactory;


    private static final String PROJECT_EXISTS_MSG = "Проект с именем \"%s\" уже существует!";
    private static final String PROJECT_NOT_FOUND_MSG = "Проект с ID \"%s\" не найден!";

    public List<ProjectDto> getAllProjects(Optional<String> optionalName) {
        Stream<Project> projects = optionalName
                .filter(name -> !name.trim().isEmpty())
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);

        return projects.map(projectDtoFactory::makeProjectDto).toList();
    }

    public ProjectDto createProject(String name) {
        validateProjectName(name);
        checkProjectNameUniqueness(name);

        Project project = projectRepository.save(
                Project.builder()
                .name(name)
                .createdAt(LocalDateTime.now())
                .build()
        );
        return projectDtoFactory.makeProjectDto(project);
    }

    public ProjectDto editProject(Long projectId, String newName) {
        validateProjectName(newName);
        Project project = getProjectOrThrowException(projectId);
        checkProjectNameUniquenessAndNotSame(newName, projectId);

        project.setName(newName);
        return projectDtoFactory.makeProjectDto(project);
    }

    public void deleteProject(Long projectId) {
        Project project = getProjectOrThrowException(projectId);
        projectRepository.delete(project);
    }

    private void validateProjectName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Имя проекта не может быть пустым!");
        }
    }

    private void checkProjectNameUniqueness(String name) {
        projectRepository.findByName(name).ifPresent(p -> {
            throw new BadRequestException(String.format(PROJECT_EXISTS_MSG, name));
        });
    }

    private void checkProjectNameUniquenessAndNotSame(String name, Long id) {
        projectRepository.findByName(name)
                .filter(projectWithSameName -> !Objects.equals(projectWithSameName.getId(), id))
                .ifPresent(projectWithSameName ->
                {
                    throw new BadRequestException(String.format("Проект с таким именем \"%s\" уже существует", name));
                });
    }

    public Project getProjectOrThrowException(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException(String.format(PROJECT_NOT_FOUND_MSG, projectId)));
    }
}

