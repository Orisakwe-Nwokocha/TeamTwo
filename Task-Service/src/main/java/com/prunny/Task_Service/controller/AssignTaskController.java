package com.prunny.Task_Service.controller;

import com.prunny.Task_Service.dto.*;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.service.TaskAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class AssignTaskController {

    private final TaskAssignmentService taskAssignmentService;


    @PostMapping("/assign_task/{taskId}/{projectId}")
    public ResponseEntity<?> assignTaskToUser(@PathVariable("taskId") Long taskId,
                                              @PathVariable("projectId")Long projectId,
                                              @RequestBody TaskUserDTO taskUserDTO) throws ResourceAlreadyExistsException, ResourceNotFoundException {

        ApiResponse<TaskResponseDTO> response = ApiResponse.<TaskResponseDTO>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .message("User assigned to task successfully")
                .data(taskAssignmentService.assignTaskToUsers(taskId, projectId, taskUserDTO))
                .build();
        return ResponseEntity.ok(response);
    }
}
