package com.prunny.project_service.controllers;

import com.prunny.project_service.dto.requests.AddTaskRequest;
import com.prunny.project_service.dto.requests.AssignTaskDTO;
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

    @PostMapping("/{id}/add-task")
    public ResponseEntity<?> addTask(@PathVariable Long id, @RequestBody AddTaskRequest request) {
        log.info("REST request to add task");
        request.setProjectId(id);
        return ResponseEntity.status(CREATED).body(projectService.addTask(request));
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<?> getAllTasksForProject(@PathVariable Long id) {
        log.info("REST request to retrieve all tasks for project");
        return ResponseEntity.ok(projectService.getAllTasksForProject(id));
    }

    @GetMapping("/{id}/tasks/{taskId}")
    public ResponseEntity<?> viewProjectTask(@PathVariable Long id, @PathVariable Long taskId) {
        log.info("REST request to retrieve a project task");
        return ResponseEntity.ok(projectService.viewProjectTask(id, taskId));
    }

    @PostMapping("/{id}/tasks/{taskId}/assign")
    public ResponseEntity<?> assignTask(@PathVariable Long id, @PathVariable Long taskId,
                                        @RequestBody AssignTaskDTO request) {
        log.info("REST request to assign a project task to users");
        return ResponseEntity.ok(projectService.assignTask(id, taskId, request));
    }

    @GetMapping("/{id}/progress")
    public ModelAndView trackProgressOfProject(@PathVariable Long id) {
        log.info("REST request to track overall progress for project");
        ProjectProgressResponse response = projectService.trackProjectProgress(id);
        ModelAndView modelAndView = new ModelAndView("project-progress");
        modelAndView.addObject("project", response.getProject());
        modelAndView.addObject("tasks", response.getTasks());
        modelAndView.addObject("overallProgress", response.getOverallProgress());
        return modelAndView;
    }

}
