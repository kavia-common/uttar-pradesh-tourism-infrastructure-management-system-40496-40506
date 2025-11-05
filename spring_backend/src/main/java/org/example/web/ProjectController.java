package org.example.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.domain.Project;
import org.example.repository.ProjectRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * CRUD endpoints for Projects with role-based security.
 */
@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Manage projects")
public class ProjectController {

    private final ProjectRepository repo;

    public ProjectController(ProjectRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    @Operation(summary = "List projects")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<Project> list() {
        return repo.findAll();
    }

    @PostMapping
    @Operation(summary = "Create project")
    @PreAuthorize("hasRole('ADMIN')")
    public Project create(@Valid @RequestBody Project p) {
        return repo.save(p);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Project> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Project> update(@PathVariable Long id, @Valid @RequestBody Project input) {
        return repo.findById(id).map(p -> {
            p.setName(input.getName());
            p.setLocation(input.getLocation());
            p.setStartDate(input.getStartDate());
            p.setEndDate(input.getEndDate());
            return ResponseEntity.ok(repo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
