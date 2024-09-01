package com.prunny.Task_Service.controller;


import com.prunny.Task_Service.dto.ApiResponse;
import com.prunny.Task_Service.dto.TaskDTO;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.enums.TaskStatus;
import com.prunny.Task_Service.exception.NotLeaderOfProjectException;
import com.prunny.Task_Service.exception.NotMemberOfProjectException;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.service.TaskManagementService;
import com.prunny.Task_Service.service.TaskProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskProgressController {

    private final TaskProgressService taskProgressService;

    private final TaskManagementService taskManagementService;

    @PostMapping("/task_progress/{taskId}/{projectId}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> updateTaskProgress(
            @PathVariable("taskId") Long taskId,
            @PathVariable("projectId") Long projectId,
            @RequestBody Map<String, String> requestBody) throws ResourceAlreadyExistsException, ResourceNotFoundException {

        String taskStatus = requestBody.get("taskStatus");

        TaskResponseDTO updatedTask = taskProgressService.updateTaskProgress(taskId, projectId, taskStatus);


        ApiResponse<TaskResponseDTO> response = ApiResponse.<TaskResponseDTO>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(updatedTask)
                .build();

        return ResponseEntity.ok(response);
    }



    @GetMapping("taskProgress/{projectId}/{taskId}")
    public ResponseEntity<?> getTaskProgressStatus(@PathVariable("projectId") Long projectId,@PathVariable("taskId") Long taskId) throws ResourceNotFoundException, NotMemberOfProjectException, NotLeaderOfProjectException {

        ApiResponse<TaskResponseDTO> response = ApiResponse.<TaskResponseDTO>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(taskManagementService.getTaskDetails(projectId,taskId))
                .build();

        return ResponseEntity.ok(response);
    }
}
