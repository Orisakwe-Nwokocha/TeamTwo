package com.prunny.project_service.services;

import com.prunny.project_service.dto.requests.AddTaskRequest;
import com.prunny.project_service.dto.requests.CreateProjectRequest;
import com.prunny.project_service.dto.responses.ApiResponse;
import com.prunny.project_service.dto.responses.ProjectDTO;
import com.prunny.project_service.dto.responses.ProjectProgressResponse;
import com.prunny.project_service.services.clients.dto.responses.TaskDTO;

import java.util.List;

public interface ProjectService {
    ApiResponse<ProjectDTO> createProject(CreateProjectRequest request);
    ApiResponse<ProjectDTO> getProjectBy(Long id);
    ApiResponse<TaskDTO> addTask(AddTaskRequest request);
    ApiResponse<List<TaskDTO>> getAllTasksForProject(Long id);

    ProjectProgressResponse trackProjectProgress(Long id);
}
