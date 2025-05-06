package com.microserviceapp.task_service.data.repositories;

import com.microserviceapp.task_service.data.entities.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStateRepository extends JpaRepository<TaskState, Long> {
    Optional<TaskState> findByName(String name);
}
