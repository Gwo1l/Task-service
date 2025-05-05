package com.microserviceapp.task_service.repositories;

import com.microserviceapp.task_service.entities.Task;
import com.microserviceapp.task_service.entities.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndTaskState(Long taskId, TaskState taskState);
}
