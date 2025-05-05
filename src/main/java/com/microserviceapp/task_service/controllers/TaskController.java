package com.microserviceapp.task_service.controllers;

import com.microserviceapp.task_service.dto.TaskDto;
import com.microserviceapp.task_service.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@RestController
public class TaskController {
    private static final String GET_TASK = "/api/projects/{project_id}/task-states/{task-state_id}/tasks";
    private static final String CREATE_TASK = "/api/projects/{project_id}/task-states/{task-state_id}/tasks";
    private static final String EDIT_TASK = "/api/projects/{project_id}/task-states/{task_state_id}/tasks/{task_id}";
    private static final String DELETE_TASK = "/api/projects/{project_id}/task-states/{task_state_id}/tasks/{task_id}";
    private final TaskService taskService;

    @GetMapping(GET_TASK)
    public List<TaskDto> getTasks(@PathVariable("project_id") Long projectId,
                                  @PathVariable("task-state_id") Long taskStateId) {
        return taskService.getTasks(projectId, taskStateId);
    }

    @PostMapping(CREATE_TASK)
    public TaskDto createTask(@PathVariable("project_id") Long projectId,
                                        @PathVariable("task-state_id") Long taskStateId,
                                        @RequestParam String taskName) {
        return taskService.createTask(projectId, taskStateId, taskName);
    }



    @PatchMapping(EDIT_TASK)
    public TaskDto editTask(@PathVariable("project_id") Long projectId,
                                      @PathVariable("task_state_id") Long taskStateId,
                                      @PathVariable("task_id") Long taskId,
                                      @RequestParam String taskName) {

        return taskService.editTask(projectId, taskStateId, taskId, taskName);
    }


    @DeleteMapping(DELETE_TASK)
    public boolean deleteTask(@PathVariable("project_id") Long projectId,
                                   @PathVariable("task_state_id") Long taskStateId,
                                   @PathVariable("task_id") Long taskId) {
        taskService.deleteTask(projectId, taskStateId, taskId);
        return true;
    }
}
