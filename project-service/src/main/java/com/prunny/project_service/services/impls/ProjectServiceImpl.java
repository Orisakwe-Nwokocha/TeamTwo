package com.prunny.project_service.services.impls;

import com.prunny.project_service.data.models.Project;
import com.prunny.project_service.data.repositories.ProjectRepository;
import com.prunny.project_service.dto.requests.CreateProjectRequest;
import com.prunny.project_service.dto.responses.ApiResponse;
import com.prunny.project_service.dto.responses.ProjectDTO;
import com.prunny.project_service.exceptions.ResourceNotFoundException;
import com.prunny.project_service.services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.time.LocalDateTime.now;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {
    private final ModelMapper modelMapper;
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ModelMapper modelMapper, ProjectRepository projectRepository) {
        this.modelMapper = modelMapper;
        this.projectRepository = projectRepository;
    }

    @Override
    public ApiResponse<ProjectDTO> createProject(CreateProjectRequest request) {
        log.info("Request to create Project by manager: {}", request.getManager());
        Project newProject = modelMapper.map(request, Project.class);
        newProject = projectRepository.save(newProject);
        ProjectDTO projectDTO = modelMapper.map(newProject, ProjectDTO.class);
        String message = "Project created successfully";
        log.info("{}: {}", message, projectDTO);
        return buildApiResponse(projectDTO, message);
    }

    @Override
    public ApiResponse<ProjectDTO> getProjectBy(Long id) {
        log.info("Request to get Project: {}", id);
        Project project = getBy(id);
        String message = "Project retrieved successfully";
        log.info("{}: {}", message, project);
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
        return buildApiResponse(projectDTO, message);
    }

    private Project getBy(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    private static <T> ApiResponse<T> buildApiResponse(T data, String message) {
        return ApiResponse.<T>builder()
                .responseTime(now())
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
}
