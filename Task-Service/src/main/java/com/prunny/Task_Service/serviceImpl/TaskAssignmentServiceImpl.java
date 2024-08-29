package com.prunny.Task_Service.serviceImpl;

import com.prunny.Task_Service.client.ProjectClient;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.dto.TaskUserDTO;
import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.repository.TaskRepository;
import com.prunny.Task_Service.service.TaskAssignmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@Slf4j
public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    private final TaskRepository taskRepository;
    private final ProjectClient projectClient;
    private final ModelMapper modelMapper;


    public TaskAssignmentServiceImpl(TaskRepository taskRepository, ProjectClient projectClient, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.projectClient = projectClient;
        this.modelMapper = modelMapper;
    }

    public TaskResponseDTO assignTaskToUsers(Long taskId,Long projectId, TaskUserDTO taskUserDTO)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {

        //confirm the project
        ResponseEntity<Map<String, Object>> response = projectClient.getProjectById(projectId);

        if (response == null || response.getBody() == null) {
            throw new ResourceNotFoundException("PROJECT DOES NOT EXIST! CREATE THE PROJECT FIRST");
        }

        // Validate Task existence
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        // Confirm and validate user email from UserService via FeignClient
//        UserDTO userDTO = userServiceClient.getUserByEmail(taskUserDTO.getUserEmail());
//        if (userDTO == null) {
//            throw new ResourceNotFoundException("User not found with email: " + taskUserDTO.getUserEmail());
//        }

        if (task.getAssignedUserEmails().contains(taskUserDTO.getUserEmail())) {
            throw new ResourceAlreadyExistsException("User is already assigned to this task.");
        }

        task.getAssignedUserEmails().add(taskUserDTO.getUserEmail());

        taskRepository.save(task);
        log.info("Task assigned to this user");

        return modelMapper.map(task, TaskResponseDTO.class);
    }




}
