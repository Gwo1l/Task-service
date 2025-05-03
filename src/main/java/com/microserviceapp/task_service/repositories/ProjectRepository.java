package com.microserviceapp.task_service.repositories;

import com.microserviceapp.task_service.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);
}
