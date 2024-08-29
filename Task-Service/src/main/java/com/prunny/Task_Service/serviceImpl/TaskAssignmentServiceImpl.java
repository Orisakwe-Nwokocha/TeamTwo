package com.prunny.Task_Service.serviceImpl;

import com.prunny.Task_Service.client.ProjectClient;
import com.prunny.Task_Service.dto.AssignTaskRequestDTO;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.dto.TaskUserDTO;
import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.entity.TaskUser;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.repository.TaskRepository;
import com.prunny.Task_Service.repository.TaskUserRepository;
import com.prunny.Task_Service.service.TaskAssignmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    private final TaskRepository taskRepository;
    private final ProjectClient projectClient;
    private final ModelMapper modelMapper;
    private final TaskUserRepository taskUserRepository;

    public TaskAssignmentServiceImpl(TaskRepository taskRepository, ProjectClient projectClient, ModelMapper modelMapper, TaskUserRepository taskUserRepository) {
        this.taskRepository = taskRepository;
        this.projectClient = projectClient;
        this.modelMapper = modelMapper;
        this.taskUserRepository = taskUserRepository;
    }

    @Override
    public TaskResponseDTO assignTaskToUsers(Long taskId, List<TaskUserDTO> assignedUsersDTO) throws ResourceNotFoundException {

        // Find the existing Task entity
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Map assigned users from DTO to TaskUser entities and associate with the Task
        List<TaskUser> assignedUsers = assignedUsersDTO.stream()
                .map(taskUserDTO -> {
                    TaskUser taskUser = new TaskUser();
                    taskUser.setUserId(taskUserDTO.getUserId());
                    taskUser.setTask(task); // Associate the TaskUser with the Task
                    return taskUser;
                })
                .collect(Collectors.toList());

        task.setAssignedUsers(assignedUsers);

        // Save the updated task entity
        taskRepository.save(task);
        log.info("Users successfully assigned to task");

        // Map the updated Task entity to TaskResponseDTO
        TaskResponseDTO taskResponseDto = modelMapper.map(task, TaskResponseDTO.class);

        return taskResponseDto;
    }




}
