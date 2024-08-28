package com.prunny.project_service.services;

import com.prunny.project_service.dto.requests.AddTaskRequest;
import com.prunny.project_service.dto.requests.CreateProjectRequest;
import com.prunny.project_service.dto.responses.ApiResponse;
import com.prunny.project_service.dto.responses.ProjectDTO;
import com.prunny.project_service.services.clients.dto.responses.TaskDTO;

public interface ProjectService {
    ApiResponse<ProjectDTO> createProject(CreateProjectRequest request);
    ApiResponse<ProjectDTO> getProjectBy(Long id);
    ApiResponse<TaskDTO> addTask(AddTaskRequest request);
}
