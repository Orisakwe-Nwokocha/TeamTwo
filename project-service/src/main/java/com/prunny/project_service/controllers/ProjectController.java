package com.prunny.project_service.controllers;

import com.prunny.project_service.dto.requests.AddTaskRequest;
import com.prunny.project_service.dto.requests.CreateProjectRequest;

import com.prunny.project_service.dto.responses.ProjectProgressResponse;

import com.prunny.project_service.services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/projects")
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody CreateProjectRequest request) {
        log.info("REST request to create Project");
        return ResponseEntity.status(CREATED).body(projectService.createProject(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        log.info("REST request to retrieve Project");
        return ResponseEntity.ok(projectService.getProjectBy(id));
    }

    @PostMapping("/add-task")
    public ResponseEntity<?> addTask(@RequestBody AddTaskRequest request) {
        log.info("REST request to add task");
        return ResponseEntity.status(CREATED).body(projectService.addTask(request));
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<?> getAllTasksForProject(@PathVariable Long id) {
        log.info("REST request to retrieve all tasks for project");
        return ResponseEntity.ok(projectService.getAllTasksForProject(id));
    }

    @GetMapping("/{id}/progress")
    public ModelAndView trackProjectOverallProgress(@PathVariable Long id) {
        log.info("REST request to track overall progress for project");
        ProjectProgressResponse response = projectService.trackProjectProgress(id);
        ModelAndView modelAndView = new ModelAndView("project-progress");
        modelAndView.addObject("project", response.getProject());
        modelAndView.addObject("tasks", response.getTasks());
        modelAndView.addObject("overallProgress", response.getOverallProgress());
        return modelAndView;
    }

}
