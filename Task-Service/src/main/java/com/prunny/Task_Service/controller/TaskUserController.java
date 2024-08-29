package com.prunny.Task_Service.controller;

import com.prunny.Task_Service.dto.*;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.service.TaskAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/task_user")
@RequiredArgsConstructor
public class TaskUserController {

    private final TaskAssignmentService taskAssignmentService;

    @PostMapping("/assign_task")
    public ResponseEntity<?> assignTaskToUser(@RequestBody AssignTaskRequestDTO assignTaskRequest) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        // Convert userIdList into a list of TaskUserDTO objects
        List<TaskUserDTO> assignedUsersDTO = assignTaskRequest.getUserIdList().stream()
                .map(userId -> {
                    TaskUserDTO taskUserDTO = new TaskUserDTO();
                    taskUserDTO.setUserId(userId);
                    return taskUserDTO;
                })
                .collect(Collectors.toList());

        // Call the service method to assign users to the task
        TaskResponseDTO taskResponse = taskAssignmentService.assignTaskToUsers(
                assignTaskRequest.getTaskId(),
                assignedUsersDTO
        );

        // Build the API response
        ApiResponse<TaskResponseDTO> response = ApiResponse.<TaskResponseDTO>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(taskResponse)
                .build();

        // Return the response entity
        return ResponseEntity.ok(response);
    }




}
