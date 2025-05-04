package com.microserviceapp.task_service.controllers.helpers;

import com.microserviceapp.task_service.entities.Project;
import com.microserviceapp.task_service.entities.TaskState;
import com.microserviceapp.task_service.exceptions.BadRequestException;
import com.microserviceapp.task_service.exceptions.NotFoundException;
import com.microserviceapp.task_service.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ControllerHelper {
    ProjectRepository projectRepository;
    @Autowired
    public ControllerHelper(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project getProjectOrThrowException(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Проект с таким id \"%s\" не существует!", id)));
    }
    public void checkExistanceOfProjectWithSameNameAndThrowException(Long id, String name) {
        projectRepository.findByName(name)
                .filter(projectWithSameName -> !Objects.equals(projectWithSameName.getId(), id))
                .ifPresent(projectWithSameName ->
                {
                    throw new BadRequestException(String.format("Проект с таким именем \"%s\" уже существует", name));
                });
    }

    public void throwExceptionIfNameIsBlank(String name) {
        if (name.isBlank()) {
            throw new BadRequestException("Поле не может быть пустым");
        }
    }

    public TaskState getTaskStateOrThrowException(Long taskStateId, Project project) {
        return project.getTaskStates()
                .stream()
                .filter(anotherTaskState -> anotherTaskState.getId().equals(taskStateId))
                .findAny()
                .orElseThrow(() -> new BadRequestException(
                        String.format("Состояния с таким id \"%s\" не существует", taskStateId
                        )));
    }
    public void throwExceptionIfExistsProjectWithSameName(String name, Project project) {
        project.getTaskStates()
                .stream().map(TaskState::getName)
                .filter(stateName -> stateName.equalsIgnoreCase(name))
                .findAny()
                .ifPresent((el) -> {
                    throw new BadRequestException("Состояние с таким именем уже существует");
                });
    }
}
