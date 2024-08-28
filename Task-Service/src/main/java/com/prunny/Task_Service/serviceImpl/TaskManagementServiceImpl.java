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

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.prunny.Task_Service.enums.TaskStatus.IN_PROGRESS;

@Service
@Slf4j
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;
    private final ProjectClient projectClient;
     ModelMapper modelMapper = new ModelMapper();
     ObjectMapper objectMapper = new ObjectMapper();

    public TaskManagementServiceImpl(TaskRepository taskRepository, ProjectClient projectClient) {
        this.taskRepository = taskRepository;
        this.projectClient = projectClient;
    }


    @Override
    public TaskResponseDTO createNewTask(TaskDTO taskRequest) throws ResourceNotFoundException, ResourceAlreadyExistsException {

        //get project by id
    //  projectClient.getProjectById(id);

        //check if genre exist first, by checking the genre service
        ResponseEntity<Map<String, Object>> response = (ResponseEntity<Map<String, Object>>) projectClient.getProjectById(taskRequest.getProjectId());

        if (response == null || response.getBody() == null) {
            throw new ResourceNotFoundException("PROJECT DOES NOT EXIST! CREATE THE PROJECT FIRST");
        }
        //use builder

        Task task = new Task();
        task.setTaskStatus(IN_PROGRESS);
        task.setDescription(taskRequest.getDescription());
        task.setTaskName(taskRequest.getTaskName());
        task.setTaskPriority(TaskPriority.valueOf(taskRequest.getTaskPriority()));
        task.setTaskStatus(TaskStatus.valueOf(taskRequest.getTaskStatus()));
        task.setCompletionDate(null);
        task.setProjectId(taskRequest.getProjectId());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setOverdue(false);
        task.setAssignedUsers(taskRequest.getAssignedUsersDTO());
        task.setDueDate(taskRequest.getDueDate());

        taskRepository.save(task);
        log.info("task successfully saved to the database");

        TaskResponseDTO taskResponseDto = modelMapper.map(task, TaskResponseDTO.class);

        ProjectDTO projectDto = objectMapper.convertValue(response.getBody().get("data"), ProjectDTO.class);

        taskResponseDto.setProjectDTO(Collections.singletonList(projectDto));

        return taskResponseDto;

    }

    @Override
    public TaskResponseDTO updateTask(Long taskId, TaskDTO taskRequest)
            throws ResourceNotFoundException, NotMemberOfProjectException, NotLeaderOfProjectException {


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

        return modelMapper.map(taskToUpdate, TaskResponseDTO.class);
    }

    @Override
    public TaskResponseDTO getTaskDetails(Long taskId) throws ResourceNotFoundException {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("task not found"));

        TaskResponseDTO taskResponse = modelMapper.map(task, TaskResponseDTO.class);
        log.info("Retrieved task details successfully");

        return taskResponse;
    }

   // long projectId,
    @Override
    public void deleteTask( long taskId) throws ResourceNotFoundException, NotLeaderOfProjectException {

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
    public List<TaskResponseDTO> searchTaskBasedOnDifferentCriteria(TaskStatus status, TaskPriority priority, Long projectId, Long assignedTo_UserId) {
        return List.of();
    }


}
