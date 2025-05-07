package com.microserviceapp.task_service.services;

import com.microserviceapp.task_service.data.dto.ProjectDto;
import com.microserviceapp.task_service.data.entities.Project;
import com.microserviceapp.task_service.data.factories.ProjectDtoFactory;
import com.microserviceapp.task_service.data.repositories.ProjectRepository;
import com.microserviceapp.task_service.exceptions.BadRequestException;
import com.microserviceapp.task_service.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectDtoFactory projectDtoFactory;


    private static final String PROJECT_EXISTS_MSG = "Проект с именем \"%s\" уже существует!";
    private static final String PROJECT_NOT_FOUND_MSG = "Проект с ID \"%s\" не найден!";

    public List<ProjectDto> getAllProjects(Optional<String> optionalName) {
        log.debug("Getting projects with name {}", optionalName.orElse("ALL"));
        Stream<Project> projects = optionalName
                .filter(name -> !name.trim().isEmpty())
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);

        return projects.map(projectDtoFactory::makeProjectDto).toList();
    }

    public ProjectDto createProject(String name) {
        log.debug("Creating project with name {}", name);
        validateProjectName(name);
        checkProjectNameUniqueness(name);

        Project project = projectRepository.save(
                Project.builder()
                .name(name)
                .createdAt(LocalDateTime.now())
                .build()
        );
        log.info("Created project with ID={} and name={}  created", project.getId(), project.getName());
        return projectDtoFactory.makeProjectDto(project);
    }

    public ProjectDto editProject(Long projectId, String newName) {
        log.debug("Editing project ID={} with new name: {}", projectId, newName);
        validateProjectName(newName);
        Project project = getProjectOrThrowException(projectId);
        checkProjectNameUniquenessAndNotSame(newName, projectId);

        project.setName(newName);
        log.info("Project name changed on name={}", project.getName());
        return projectDtoFactory.makeProjectDto(project);
    }

    public void deleteProject(Long projectId) {
        log.warn("Deleting project with ID={}", projectId);
        Project project = getProjectOrThrowException(projectId);
        projectRepository.delete(project);
        log.info("Project deleted: ID={}", projectId);
    }

    private void validateProjectName(String name) {
        if (name == null || name.isBlank()) {
            log.error("Project with empty name");
            throw new BadRequestException("Имя проекта не может быть пустым!");
        }
    }

    private void checkProjectNameUniqueness(String name) {
        projectRepository.findByName(name).ifPresent(p -> {
            log.error("Project name '{}' already exists", name);
            throw new BadRequestException(String.format(PROJECT_EXISTS_MSG, name));
        });
    }

    private void checkProjectNameUniquenessAndNotSame(String name, Long id) {
        projectRepository.findByName(name)
                .filter(projectWithSameName -> !Objects.equals(projectWithSameName.getId(), id))
                .ifPresent(projectWithSameName ->
                {
                    log.error("Project name '{}' already exists", name);
                    throw new BadRequestException(String.format("Проект с таким именем \"%s\" уже существует", name));
                });
    }

    public Project getProjectOrThrowException(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isEmpty()) {
            log.error("Project not found: ID={}", projectId);
            //throw new RuntimeException("pupu");
            throw new NotFoundException(String.format(PROJECT_NOT_FOUND_MSG, projectId));
        }
        return project.get();
    }
}

