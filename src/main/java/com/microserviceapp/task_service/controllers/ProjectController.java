package com.microserviceapp.task_service.controllers;

import com.microserviceapp.task_service.dto.ProjectDto;
import com.microserviceapp.task_service.entities.Project;
import com.microserviceapp.task_service.exceptions.BadRequestException;
import com.microserviceapp.task_service.exceptions.NotFoundException;
import com.microserviceapp.task_service.factories.ProjectDtoFactory;
import com.microserviceapp.task_service.repositories.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Transactional
@RestController
public class ProjectController {
    private ProjectRepository projectRepository;
    private ProjectDtoFactory projectDtoFactory;
    public final String CREATE_PROJECT = "/api/projects";
    public final String EDIT_PROJECT = "/api/projects/{project_id}";
    @Autowired
    public ProjectController(ProjectRepository projectRepository, ProjectDtoFactory projectDtoFactory) {
        this.projectRepository = projectRepository;
        this.projectDtoFactory = projectDtoFactory;
    }

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(@RequestParam String name) {
        projectRepository.findByName(name)
                .ifPresent(project -> {throw new BadRequestException("Такой проект уже существует!");});

        Project project = projectRepository.saveAndFlush(new Project(name));
        return projectDtoFactory.makeProjectDto(project);
    }
    @PatchMapping(EDIT_PROJECT)
    public ProjectDto editProject(@PathVariable("project_id") Long id, @RequestParam String name) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Проект с таким id \"%s\" не существует!", id)));



        project = projectRepository.saveAndFlush(new Project(name));
        return projectDtoFactory.makeProjectDto(project);
    }
}
