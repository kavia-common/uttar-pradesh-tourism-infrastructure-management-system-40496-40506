package org.example.repository;

import org.example.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PUBLIC_INTERFACE
 * Repository for Project entity.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
