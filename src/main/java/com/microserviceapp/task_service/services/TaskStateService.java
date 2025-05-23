package com.microserviceapp.task_service.services;

import com.microserviceapp.task_service.data.dto.TaskStateDto;
import com.microserviceapp.task_service.data.entities.Project;
import com.microserviceapp.task_service.data.entities.TaskState;
import com.microserviceapp.task_service.exceptions.BadRequestException;
import com.microserviceapp.task_service.exceptions.NotFoundException;
import com.microserviceapp.task_service.data.factories.TaskStateDtoFactory;
import com.microserviceapp.task_service.data.repositories.TaskStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TaskStateService {

    private final TaskStateRepository taskStateRepository;
    private final TaskStateDtoFactory taskStateDtoFactory;
    private final ProjectService projectService;
    private static final String TASK_STATE_EXISTS_MSG = "Состояние с именем \"%s\" уже существует в проекте!";
    private static final String TASK_STATE_NOT_FOUND_MSG = "Состояние с ID \"%s\" не найдено!";
    private static final String NAME_BLANK_MSG = "Имя состояния не может быть пустым!";

    public List<TaskStateDto> getTaskStates(Long projectId) {
        log.debug("Getting task states for project ID={}", projectId);
        Project project = projectService.getProjectOrThrowException(projectId);
        return project.getTaskStates()
                .stream()
                .map(taskStateDtoFactory::makeTaskStateDto)
                .toList();
    }

    public TaskStateDto createTaskState(Long projectId, String name) {
        log.debug("Creating task state in project ID={}. Name={}", projectId, name);
        validateName(name);
        Project project = projectService.getProjectOrThrowException(projectId);
        checkTaskStateNameUniqueness(name, project);

        TaskState taskState = taskStateRepository.save(
                TaskState.builder()
                        .name(name)
                        .createdAt(LocalDateTime.now())
                        .project(project)
                        .build()
        );

        project.getTaskStates().add(taskState);
        log.info("Created task state with id= {} and name'{}' in project {}", taskState.getId(), name, projectId);
        return taskStateDtoFactory.makeTaskStateDto(taskState);
    }
    //TODO: добавить обновление времени updatedAt
    public TaskStateDto editTaskState(Long projectId, Long taskStateId, String newName) {
        log.debug("Editing task state with ID={} in project ID={} on new name={}", taskStateId, projectId, newName);
        validateName(newName);
        Project project = projectService.getProjectOrThrowException(projectId);
        TaskState taskState = getTaskStateOrThrowException(taskStateId, project);
        checkTaskStateNameUniquenessAndNotSame(newName, project, taskStateId);

        taskState.setName(newName);
        log.info("Task state with ID={} in project {} changed name on {}", taskState, projectId, newName);
        return taskStateDtoFactory.makeTaskStateDto(taskState);
    }

    public void deleteTaskState(Long projectId, Long taskStateId) {
        log.warn("Deleting task state with ID={} from project {}", taskStateId, projectId);
        Project project = projectService.getProjectOrThrowException(projectId);
        TaskState taskState = getTaskStateOrThrowException(taskStateId, project);
        taskStateRepository.delete(taskState);
        log.info("Deleted task state with ID={} from project {}", taskStateId, projectId);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            log.error("Task state name is empty");
            throw new BadRequestException(NAME_BLANK_MSG);
        }
    }

    public TaskState getTaskStateOrThrowException(Long taskStateId, Project project) {
        return project.getTaskStates().stream()
                .filter(ts -> ts.getId().equals(taskStateId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        String.format(TASK_STATE_NOT_FOUND_MSG, taskStateId)
                ));
    }

    private void checkTaskStateNameUniqueness(String name, Project project) {
        project.getTaskStates().stream()
                .filter(ts -> ts.getName().equalsIgnoreCase(name))
                .findAny()
                .ifPresent(ts -> {
                    log.error("Task state name={} already exists", name);
                    throw new BadRequestException(String.format(TASK_STATE_EXISTS_MSG, name));
                });
    }

    private void checkTaskStateNameUniquenessAndNotSame(String name, Project project, Long excludeTaskStateId) {
        project.getTaskStates().stream()
                .filter(ts -> !ts.getId().equals(excludeTaskStateId))
                .filter(ts -> ts.getName().equalsIgnoreCase(name))
                .findAny()
                .ifPresent(ts -> {
                    log.error("Task state name={} already exists", name);
                    throw new BadRequestException(String.format(TASK_STATE_EXISTS_MSG, name));
                });
    }
}
