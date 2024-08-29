package com.prunny.Task_Service.serviceImpl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.prunny.Task_Service.client.ProjectClient;
import com.prunny.Task_Service.dto.ProjectDTO;

import com.prunny.Task_Service.dto.TaskDTO;
import com.prunny.Task_Service.dto.TaskRequestDTO;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.enums.TaskPriority;
import com.prunny.Task_Service.enums.TaskStatus;
import com.prunny.Task_Service.exception.NotLeaderOfProjectException;
import com.prunny.Task_Service.exception.NotMemberOfProjectException;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.repository.TaskRepository;
import com.prunny.Task_Service.service.TaskManagementService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.prunny.Task_Service.enums.TaskStatus.TO_DO;

@Service
@Slf4j
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;

    ModelMapper modelMapper = new ModelMapper();

    private final ProjectClient projectClient;

     ObjectMapper objectMapper = new ObjectMapper();


    public TaskManagementServiceImpl(TaskRepository taskRepository, ProjectClient projectClient) {
        this.taskRepository = taskRepository;
        this.projectClient = projectClient;
    }


    @Override
    public TaskResponseDTO createNewTask(TaskRequestDTO taskRequest) throws ResourceNotFoundException, ResourceAlreadyExistsException {

        //TODO: no need for validation since this call is only made through the project service
        //TODO: the validation of a project is done in the project service
       //VALIDATION
//        ResponseEntity<Map<String, Object>> response = projectClient.getProjectById(taskRequest.getProjectId());
//
//
//        if (response == null || response.getBody() == null) {
//            throw new ResourceNotFoundException("PROJECT DOES NOT EXIST! CREATE THE PROJECT FIRST");
//        }

        Task task = modelMapper.map(taskRequest, Task.class);
        task.setTaskStatus(TO_DO);
        task.setTaskName(taskRequest.getTaskName());
        task.setDescription(taskRequest.getDescription());
        task.setTaskPriority(taskRequest.getTaskPriority());
        task.setCreatedAt(LocalDateTime.now());
        task.setDueDate(taskRequest.getDueDate());
        task.setProjectId(taskRequest.getProjectId());

        task = taskRepository.save(task);
        log.info("task successfully saved to the database");

        taskRepository.save(task);
        log.info("Task successfully created and saved to the database");


        // Extract project information from the response
//        ProjectDTO projectDTO = objectMapper.convertValue(response.getBody().get("data"), ProjectDTO.class);
//        taskResponseDto.setProjectDTO(Collections.singletonList(projectDTO));

        return modelMapper.map(task, TaskResponseDTO.class);
    }



    @Override
    public TaskResponseDTO updateTask(Long taskId, TaskDTO taskRequest)
            throws ResourceNotFoundException, NotMemberOfProjectException, NotLeaderOfProjectException {

        //VALIDATION
        ResponseEntity<Map<String, Object>> response = projectClient.getProjectById(taskRequest.getProjectId());


        if (response == null || response.getBody() == null) {
            throw new ResourceNotFoundException("PROJECT DOES NOT EXIST! CREATE THE PROJECT FIRST");
        }


        Task taskToUpdate = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskToUpdate.setTaskStatus(TaskStatus.valueOf(taskRequest.getTaskStatus()));
        taskToUpdate.setTaskName(taskRequest.getTaskName());
        taskToUpdate.setProjectId(taskRequest.getProjectId());
        taskToUpdate.setTaskPriority(TaskPriority.valueOf(taskRequest.getTaskPriority()));
        taskToUpdate.setDueDate(taskRequest.getDueDate());
        taskToUpdate.setUpdatedAt(LocalDateTime.now());
        taskToUpdate.setDescription(taskRequest.getDescription());
        taskToUpdate.setCompletionDate(LocalDateTime.now());

        taskRepository.save(taskToUpdate);
        log.info("Edited task successfully saved to the database");

        // Extract project information from the response
//        ProjectDTO projectDTO = objectMapper.convertValue(response.getBody().get("data"), ProjectDTO.class);
//        taskResponseDto.setProjectDTO(Collections.singletonList(projectDTO));

        return modelMapper.map(taskToUpdate, TaskResponseDTO.class);
    }

    @Override
    public TaskResponseDTO getTaskDetails(Long projectId, Long taskId) throws ResourceNotFoundException {

        //VALIDATION
        ResponseEntity<Map<String, Object>> response = projectClient.getProjectById(projectId);


        if (response == null || response.getBody() == null) {
            throw new ResourceNotFoundException("PROJECT DOES NOT EXIST! CREATE THE PROJECT FIRST");
        }

        // Fetch the task by ID
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID " + taskId + " not found."));


        TaskResponseDTO taskResponse = modelMapper.map(task, TaskResponseDTO.class);
        log.info("Retrieved task details successfully for Task ID: {}", taskId);

        // Extract project information from the response
//        ProjectDTO projectDTO = objectMapper.convertValue(response.getBody().get("data"), ProjectDTO.class);
//        taskResponse.setProjectDTO(Collections.singletonList(projectDTO));


        return taskResponse;
    }

   // long projectId,
    @Override
    public void deleteTask(Long projectId,long taskId) throws ResourceNotFoundException, NotLeaderOfProjectException {

        projectClient.getProjectById(projectId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("task not found"));

        taskRepository.delete(task);
        log.info("Task successfully deleted");

    }

    @Override
    public List<TaskResponseDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll(); // Adjust this if you have a specific query to get tasks

        // Convert the list of Task entities to a list of TaskResponseDTO
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDTO> searchTaskBasedOnDifferentCriteria(TaskStatus status, TaskPriority priority, Long projectId) {

        List<Task> tasks;

        if (status != null && priority != null && projectId != null) {
            tasks = taskRepository.findByTaskStatusAndTaskPriorityAndProjectId(status, priority, projectId);
        } else if (status != null && priority != null) {
            tasks = taskRepository.findByTaskStatusAndTaskPriority(status, priority);
        } else if (status != null && projectId != null) {
            tasks = taskRepository.findByTaskStatusAndProjectId(status, projectId);
        } else if (priority != null && projectId != null) {
            tasks = taskRepository.findByTaskPriorityAndProjectId(priority, projectId);
        } else if (status != null) {
            tasks = taskRepository.findByTaskStatus(status);
        } else if (priority != null) {
            tasks = taskRepository.findByTaskPriority(priority);
        } else if (projectId != null) {
            tasks = taskRepository.findByProjectId(projectId);
        } else {
            tasks = new ArrayList<>(); // No criteria provided
        }

        // Convert tasks to DTOs using stream
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskResponseDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<TaskDTO> getAllTasksForProject(Long projectId) throws ResourceNotFoundException {
        if (!taskRepository.existsByProjectId(projectId))
            throw new ResourceNotFoundException("Task does not exist for project");
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        log.info("Retrieved tasks successfully");
        return List.of(modelMapper.map(tasks, TaskDTO[].class));
    }



}
