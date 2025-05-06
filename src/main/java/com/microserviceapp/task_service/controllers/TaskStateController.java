package com.microserviceapp.task_service.controllers;

import com.microserviceapp.task_service.data.dto.TaskStateDto;
import com.microserviceapp.task_service.services.TaskStateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@RestController
public class TaskStateController {
    private static final String GET_TASK_STATES = "/api/projects/{project_id}/task-states";
    private static final String CREATE_TASK_STATE = "/api/projects/{project_id}/task-states";
    private static final String EDIT_TASK_STATE = "/api/projects/{project_id}/task-states/{task_state_id}";
    private static final String DELETE_TASK_STATE = "/api/projects/{project_id}/task-states/{task_state_id}";
    private final TaskStateService taskStateService;

    @GetMapping(GET_TASK_STATES)
    public List<TaskStateDto> getTaskStates(@PathVariable("project_id") Long projectId) {
        return taskStateService.getTaskStates(projectId);
    }

    @PostMapping(CREATE_TASK_STATE)
    public TaskStateDto createTaskState(@PathVariable("project_id") Long projectId, @RequestParam String name) {
        return taskStateService.createTaskState(projectId, name);
    }



    @PatchMapping(EDIT_TASK_STATE)
    public TaskStateDto editTaskState(@PathVariable("project_id") Long projectId,
                                      @PathVariable("task_state_id") Long taskStateId,
                                      @RequestParam String name) {

        return taskStateService.editTaskState(projectId, taskStateId, name);
    }


    @DeleteMapping(DELETE_TASK_STATE)
    public boolean deleteTaskState(@PathVariable("project_id") Long projectId,
                                   @PathVariable("task_state_id") Long taskStateId) {
        taskStateService.deleteTaskState(projectId, taskStateId);
        return true;
    }
}
