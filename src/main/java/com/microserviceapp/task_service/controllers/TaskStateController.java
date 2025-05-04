package com.microserviceapp.task_service.controllers;

import com.microserviceapp.task_service.controllers.helpers.ControllerHelper;
import com.microserviceapp.task_service.dto.ProjectDto;
import com.microserviceapp.task_service.dto.TaskStateDto;
import com.microserviceapp.task_service.entities.Project;
import com.microserviceapp.task_service.entities.TaskState;
import com.microserviceapp.task_service.exceptions.BadRequestException;
import com.microserviceapp.task_service.factories.TaskStateDtoFactory;
import com.microserviceapp.task_service.repositories.ProjectRepository;
import com.microserviceapp.task_service.repositories.TaskStateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
@Transactional
@RestController
public class TaskStateController {
    private static final String GET_TASK_STATES = "/api/projects/{project_id}/task-states";
    private static final String CREATE_TASK_STATE = "/api/projects/{project_id}/task-states";
    private static final String EDIT_TASK_STATE = "/api/projects/{project_id}/task-states/{task_state_id}";
    private static final String DELETE_TASK_STATE = "/api/projects/{project_id}/task-states/{task_state_id}";
    private TaskStateRepository taskStateRepository;
    private ProjectRepository projectRepository;
    private ControllerHelper controllerHelper;
    private TaskStateDtoFactory taskStateDtoFactory;
    @Autowired
    public TaskStateController(TaskStateRepository taskStateRepository,
                               ProjectRepository projectRepository,
                               ControllerHelper controllerHelper,
                               TaskStateDtoFactory taskStateDtoFactory) {
        this.taskStateRepository = taskStateRepository;
        this.projectRepository = projectRepository;
        this.controllerHelper = controllerHelper;
        this.taskStateDtoFactory = taskStateDtoFactory;
    }

    @GetMapping(GET_TASK_STATES)
    public List<TaskStateDto> getTaskStates(@PathVariable("project_id") Long projectId) {
        Project project = controllerHelper.getProjectOrThrowException(projectId);
        return project.getTaskStates()
                .stream().map(taskStateDtoFactory::makeTaskStateDto).collect(Collectors.toList());
    }

    @PostMapping(CREATE_TASK_STATE)
    public TaskStateDto createTaskState(@PathVariable("project_id") Long projectId, @RequestParam String name) {
        controllerHelper.throwExceptionIfNameIsBlank(name);
        Project project = controllerHelper.getProjectOrThrowException(projectId);
        controllerHelper.throwExceptionIfExistsProjectWithSameName(name, project);

        TaskState taskState = TaskState.builder()
                .name(name)
                .createdAt(LocalDateTime.now())
                .project(project)
                .build();
        taskStateRepository.saveAndFlush(taskState);
        project.getTaskStates().add(taskState);
        return taskStateDtoFactory.makeTaskStateDto(taskState);
    }



    @PatchMapping(EDIT_TASK_STATE)
    public TaskStateDto editTaskState(@PathVariable("project_id") Long projectId,
                                  @PathVariable("task_state_id") Long taskStateId,
                                  @RequestParam String name) {

        controllerHelper.throwExceptionIfNameIsBlank(name);
        Project project = controllerHelper.getProjectOrThrowException(projectId);
        controllerHelper.throwExceptionIfExistsProjectWithSameName(name, project);

        TaskState taskState = controllerHelper.getTaskStateOrThrowException(taskStateId, project);

        taskState.setName(name);
        taskStateRepository.saveAndFlush(taskState);
        return taskStateDtoFactory.makeTaskStateDto(taskState);
    }


    @DeleteMapping(DELETE_TASK_STATE)
    public boolean deleteTaskState(@PathVariable("project_id") Long projectId,
                                 @PathVariable("task_state_id") Long taskStateId) {
        Project project = controllerHelper.getProjectOrThrowException(projectId);
        TaskState taskState = controllerHelper.getTaskStateOrThrowException(taskStateId, project);
        taskStateRepository.delete(taskState);
        return true;
    }
}
