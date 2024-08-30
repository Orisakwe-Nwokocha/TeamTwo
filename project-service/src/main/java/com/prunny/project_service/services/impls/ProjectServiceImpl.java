package com.prunny.project_service.services.impls;

import com.prunny.project_service.data.models.Project;
import com.prunny.project_service.data.repositories.ProjectRepository;
import com.prunny.project_service.dto.requests.AddTaskRequest;
import com.prunny.project_service.dto.requests.AssignTaskDTO;
import com.prunny.project_service.dto.requests.CreateProjectRequest;
import com.prunny.project_service.dto.responses.ApiResponse;
import com.prunny.project_service.dto.responses.ProjectDTO;
import com.prunny.project_service.dto.responses.ProjectProgressResponse;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public ApiResponse<List<TaskDTO>> getAllTasksForProject(Long id) {
        log.info("Request to retrieve tasks for Project: {}", id);
        validateProject(id);
        var tasks = fetchTasksFromTaskServiceByProject(id);
        String message = "Tasks retrieved successfully for Project: " + id;
        log.info("{}", message);
        return buildApiResponse(tasks, message);
    }

    @Override
    public ProjectProgressResponse trackProjectProgress(Long id) {
        log.info("Request to track project progress for Project: {}", id);
        Project project = getBy(id);
        ProjectProgressResponse response = new ProjectProgressResponse();
        response.setProject(project);
        if (project.getTaskIDs().isEmpty()) {
            log.info("No tasks found for Project: {}", id);
            response.setOverallProgress("0");
            response.setTasks(List.of());
            return response;
        }
        var tasks = fetchTasksFromTaskServiceByProject(id);
        long completedTasks = tasks.stream()
                                    .filter(task -> task.getTaskStatus().equals("COMPLETED"))
                                    .count();
        double percentage = ((double) completedTasks / tasks.size()) * 100;
        String overallProgress = String.format("%.0f", percentage);
        response.setOverallProgress(overallProgress);
        response.setTasks(tasks);
        log.info("Project overall progress: {}", overallProgress);
        return response;
    }

    @Override
    public ApiResponse<TaskResponseDTO> viewProjectTask(Long id, Long taskId) {
        log.info("Request to view task: {} for Project: {}", taskId, id);
        validateProject(id);
        TaskResponseDTO response = taskServiceClient.getProjectTask(id, taskId).getData();
        log.info("Task retrieved successfully from task service: {}", response);
        return buildApiResponse(response, "Task retrieved successfully");
    }

    @Override
    @Transactional
    public ApiResponse<TaskResponseDTO> assignTask(Long id, Long taskId, AssignTaskDTO request) {
        log.info("Request to assign task: {}, to members: {}; for Project: {}", taskId, request.getUserEmails(), id);
        Project project = getBy(id);
        project.getTeamMembers().addAll(request.getUserEmails());
        projectRepository.save(project);
        TaskResponseDTO response = taskServiceClient.assignTask(taskId, id, request).getData();
        String message = "Task assigned successfully";
        log.info(message);
        return buildApiResponse(response, message);
    }

    private void validateProject(Long id) {
        if (!projectRepository.existsById(id))
            throw new ResourceNotFoundException("Project with id " + id + " not found");
    }

    private List<TaskDTO> fetchTasksFromTaskServiceByProject(Long id) {
        log.info("Fetching all tasks from task service");
        var tasks = taskServiceClient.getAllTasksForProject(id).getData();
        log.info("Tasks retrieved successfully from task service");
        return tasks;
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
