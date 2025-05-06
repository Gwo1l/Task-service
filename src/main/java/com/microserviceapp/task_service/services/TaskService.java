package com.microserviceapp.task_service.services;

import com.microserviceapp.task_service.data.dto.TaskDto;
import com.microserviceapp.task_service.data.entities.Project;
import com.microserviceapp.task_service.data.entities.Task;
import com.microserviceapp.task_service.data.entities.TaskState;
import com.microserviceapp.task_service.exceptions.BadRequestException;
import com.microserviceapp.task_service.exceptions.NotFoundException;
import com.microserviceapp.task_service.data.factories.TaskDtoFactory;
import com.microserviceapp.task_service.data.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class TaskService {
    private static final String TASK_EXISTS_MSG = "Задание с таким именем \"%s\" уже существует";
    private static final String NAME_BLANK_MSG = "Название задания не может быть пустым";
    private static final String TASK_NOT_FOUND_MSG = "Задания с таким Id \"%s\" не существует";
    private final ProjectService projectService;
    private final TaskStateService taskStateService;
    private final TaskRepository taskRepository;
    private final TaskDtoFactory taskDtoFactory;
    public List<TaskDto> getTasks(Long projectId, Long taskStateId) {
        TaskState taskState = getTaskStateFromProjectOrThrowException(projectId, taskStateId);
        return taskState.getTasks().stream().map(taskDtoFactory::makeTaskDto).toList();
    }

    public TaskDto createTask(Long projectId, Long taskStateId, String taskName) {
        validateName(taskName);
        TaskState taskState = getTaskStateFromProjectOrThrowException(projectId, taskStateId);
        checkTaskNameUniqueness(taskName, taskState);

        Task task = taskRepository.save(
                Task.builder()
                        .name(taskName)
                        .createdAt(LocalDateTime.now())
                        .taskState(taskState)
                        .build());
        taskState.getTasks().add(task);
        return taskDtoFactory.makeTaskDto(task);
    }

    public TaskDto editTask(Long projectId, Long taskStateId, Long taskId, String taskName) {
        validateName(taskName);
        TaskState taskState = getTaskStateFromProjectOrThrowException(projectId, taskStateId);
        checkTaskNameUniquenessAndNotSame(taskName, taskState, taskId);
        Task task = getTaskOrThrowException(taskId, taskState);

        task.setName(taskName);
        return taskDtoFactory.makeTaskDto(task);
    }


    public void deleteTask(Long projectId, Long taskStateId, Long taskId) {
        TaskState taskState = getTaskStateFromProjectOrThrowException(projectId, taskStateId);
        Task task = getTaskOrThrowException(taskId, taskState);
        taskRepository.delete(task);
    }
    private TaskState getTaskStateFromProjectOrThrowException(Long projectId, Long taskStateId) {
        Project project = projectService.getProjectOrThrowException(projectId);
        TaskState taskState = taskStateService.getTaskStateOrThrowException(taskStateId, project);
        return taskState;
    }

    private Task getTaskOrThrowException(Long taskId, TaskState taskState) {
        //может быть проблема если задача удалена но осталась в taskState.getTasks()
        //не изменяю т.к. не хочу делать лишние запросы в бд
        return taskState.getTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        String.format(TASK_NOT_FOUND_MSG, taskId)
                ));
    }
    private void checkTaskNameUniqueness(String taskName, TaskState taskState) {
        taskState.getTasks().stream().map(Task::getName)
                .filter(anotherName -> anotherName.equalsIgnoreCase(taskName))
                .findAny()
                .ifPresent(el -> {throw new BadRequestException(String.format(TASK_EXISTS_MSG, taskName));});
    }
    private void checkTaskNameUniquenessAndNotSame(String taskName, TaskState taskState, Long excludeTaskId) {
        taskState.getTasks().stream()
                .filter(task -> !task.getId().equals(excludeTaskId))
                .map(Task::getName)
                .filter(anotherName -> anotherName.equalsIgnoreCase(taskName))
                .findAny()
                .ifPresent(el -> {throw new BadRequestException(String.format(TASK_EXISTS_MSG, taskName));});
    }
    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException(NAME_BLANK_MSG);
        }
    }
}
