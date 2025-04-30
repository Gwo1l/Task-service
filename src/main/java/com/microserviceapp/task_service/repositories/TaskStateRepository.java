package com.microserviceapp.task_service.repositories;

import com.microserviceapp.task_service.entities.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStateRepository extends JpaRepository<TaskState, Long> {

}
