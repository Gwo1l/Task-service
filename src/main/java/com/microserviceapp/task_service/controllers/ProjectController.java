package com.microserviceapp.task_service.controllers;

import com.microserviceapp.task_service.controllers.helpers.ControllerHelper;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional
@RestController
public class ProjectController {
    private static final String DELETE_PROJECT = "/apr/projects/{project_id}";
    public final static String CREATE_PROJECT = "/api/projects";
    public final static String EDIT_PROJECT = "/api/projects/{project_id}";
    public final static String GET_PROJECTS = "/api/projects";
    private ProjectRepository projectRepository;
    private ProjectDtoFactory projectDtoFactory;
    private ControllerHelper controllerHelper;

    @Autowired
    public ProjectController(ProjectRepository projectRepository, ProjectDtoFactory projectDtoFactory,
                             ControllerHelper controllerHelper) {
        this.projectRepository = projectRepository;
        this.projectDtoFactory = projectDtoFactory;
        this.controllerHelper = controllerHelper;
    }
    @GetMapping(GET_PROJECTS)
    public List<ProjectDto> getProjects(@RequestParam(required = false) Optional<String> optionalName) {
        optionalName = optionalName.filter(name -> !name.trim().isEmpty());
        Stream<Project> streamProjects = optionalName.map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);
        return streamProjects.map(projectDtoFactory::makeProjectDto).collect(Collectors.toList());
    }

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(@RequestParam String name) {
        projectRepository.findByName(name)
                .ifPresent(project -> {throw new BadRequestException("Такой проект уже существует!");});

        Project project = projectRepository.saveAndFlush(Project.builder().name(name).build());
        return projectDtoFactory.makeProjectDto(project);
    }
    @PatchMapping(EDIT_PROJECT)
    public ProjectDto editProject(@PathVariable("project_id") Long id, @RequestParam String name) {
        Project project = controllerHelper.getProjectOrThrowException(id);

        controllerHelper.checkExistanceOfProjectWithSameNameAndThrowException(id, name);

        project.setName(name);
        return projectDtoFactory.makeProjectDto(project);
    }

    @DeleteMapping(DELETE_PROJECT)
    public boolean deleteProject(@PathVariable("project_id") Long id) {
        Project project = controllerHelper.getProjectOrThrowException(id);

        projectRepository.delete(project);
        return true;
    }


}
