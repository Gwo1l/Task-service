package com.microserviceapp.task_service.data.repositories;

import com.microserviceapp.task_service.data.entities.Task;
import com.microserviceapp.task_service.data.entities.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndTaskState(Long taskId, TaskState taskState);
}
