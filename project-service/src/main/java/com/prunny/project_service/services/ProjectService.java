package com.prunny.project_service.services;

import com.prunny.project_service.dto.requests.CreateProjectRequest;
import com.prunny.project_service.dto.responses.ApiResponse;
import com.prunny.project_service.dto.responses.ProjectDTO;

public interface ProjectService {
    ApiResponse<ProjectDTO> createProject(CreateProjectRequest request);
    ApiResponse<ProjectDTO> getProjectBy(Long id);
}
