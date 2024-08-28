package com.prunny.project_service.data.repositories;

import com.prunny.project_service.data.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
