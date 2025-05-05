package com.microserviceapp.task_service.controllers;

import com.microserviceapp.task_service.dto.ProjectDto;
import com.microserviceapp.task_service.services.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@RestController
public class ProjectController {
    private static final String DELETE_PROJECT = "/apr/projects/{project_id}";
    public final static String CREATE_PROJECT = "/api/projects";
    public final static String EDIT_PROJECT = "/api/projects/{project_id}";
    public final static String GET_PROJECTS = "/api/projects";
    private final ProjectService projectService;

    @GetMapping(GET_PROJECTS)
    public List<ProjectDto> getProjects(@RequestParam(required = false) Optional<String> optionalName) {
        return projectService.getAllProjects(optionalName);
    }

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(@RequestParam String name) {
        return projectService.createProject(name);
    }

    @PatchMapping(EDIT_PROJECT)
    public ProjectDto editProject(@PathVariable("project_id") Long projectId,
                                  @RequestParam String name) {
        return projectService.editProject(projectId, name);
    }

    @DeleteMapping(DELETE_PROJECT)
    public boolean deleteProject(@PathVariable("project_id") Long projectId) {
        projectService.deleteProject(projectId);
        return true;
    }
}
