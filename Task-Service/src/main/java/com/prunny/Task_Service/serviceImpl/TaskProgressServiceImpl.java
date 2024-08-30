package com.prunny.Task_Service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prunny.Task_Service.client.ProjectClient;
import com.prunny.Task_Service.dto.ProjectDTO;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.enums.TaskStatus;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.repository.TaskRepository;
import com.prunny.Task_Service.service.TaskProgressService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class TaskProgressServiceImpl implements TaskProgressService {

    private final ProjectClient projectClient;
    private final TaskRepository taskRepository;
    ModelMapper modelMapper = new ModelMapper();

    public TaskProgressServiceImpl(ProjectClient projectClient, TaskRepository taskRepository) {
        this.projectClient = projectClient;
        this.taskRepository = taskRepository;
    }


    @Override
    public TaskResponseDTO updateTaskProgress(Long taskId, Long projectId, TaskStatus taskStatus) throws ResourceNotFoundException, ResourceAlreadyExistsException {


        ResponseEntity<Map<String, Object>> response = projectClient.getProjectById(projectId);
        if (response == null || response.getBody() == null) {
            throw new ResourceNotFoundException("PROJECT DOES NOT EXIST! CREATE THE PROJECT FIRST");
        }

        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new ResourceNotFoundException("Task not found"));

        if (task.getTaskStatus() == TaskStatus.COMPLETED) {
            throw new ResourceAlreadyExistsException("Task is already completed");
        }

        task.setOverdue(task.getDueDate().isBefore(LocalDateTime.now()));
        task.setTaskStatus(taskStatus);
        task.setCompletionDate(LocalDateTime.now());

        taskRepository.save(task);
        log.info("Task status updated");

        return modelMapper.map(task, TaskResponseDTO.class);
    }

}
