package com.microserviceapp.task_service.repositories;

import com.microserviceapp.task_service.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
