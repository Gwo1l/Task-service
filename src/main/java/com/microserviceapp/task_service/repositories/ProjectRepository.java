package com.microserviceapp.task_service.repositories;

import com.microserviceapp.task_service.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
