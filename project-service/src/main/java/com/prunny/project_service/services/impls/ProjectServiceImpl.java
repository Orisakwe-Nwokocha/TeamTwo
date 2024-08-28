package com.prunny.project_service.services.impls;

import com.prunny.project_service.data.models.Project;
import com.prunny.project_service.data.repositories.ProjectRepository;
import com.prunny.project_service.dto.requests.AddTaskRequest;
import com.prunny.project_service.dto.requests.CreateProjectRequest;
import com.prunny.project_service.dto.responses.ApiResponse;
import com.prunny.project_service.dto.responses.ProjectDTO;
import com.prunny.project_service.exceptions.ResourceNotFoundException;
import com.prunny.project_service.services.ProjectService;
import com.prunny.project_service.services.clients.TaskServiceClient;
import com.prunny.project_service.services.clients.dto.requests.TaskRequestDTO;
import com.prunny.project_service.services.clients.dto.responses.TaskDTO;
import com.prunny.project_service.services.clients.dto.responses.TaskResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static java.time.LocalDateTime.now;

@Service
@Slf4j
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ModelMapper modelMapper;
    private final ProjectRepository projectRepository;
    private final TaskServiceClient taskServiceClient;


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

    @Override
    public ApiResponse<TaskDTO> addTask(AddTaskRequest request) {
        Long projectId = request.getProjectId();
        log.info("Request to add task for Project: {}", projectId);
        TaskDTO taskDTO = updateProjectAndGetTaskDTO(request, projectId);
        String message = "Task created successfully for Project " + projectId;
        taskDTO.setProjectId(projectId);
        log.info("{} : {}", message, taskDTO);
        return buildApiResponse(taskDTO, message);
    }

    private TaskDTO updateProjectAndGetTaskDTO(AddTaskRequest request, Long projectId) {
        Project project = getBy(projectId);
        TaskResponseDTO taskResponseDTO = fetchTaskResponseDTO(request);
        log.info("Task response successfully fetched from task-service");
        project.getTaskIDs().add(taskResponseDTO.getTaskId());
        projectRepository.save(project);
        return modelMapper.map(taskResponseDTO, TaskDTO.class);
    }

    private TaskResponseDTO fetchTaskResponseDTO(AddTaskRequest request) {
        TaskRequestDTO taskRequestDTO = modelMapper.map(request, TaskRequestDTO.class);
        log.info("Request to fetch task response from task-service");
        return taskServiceClient.createTask(taskRequestDTO).getData();
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
