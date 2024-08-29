package com.prunny.Task_Service.controller;


import com.prunny.Task_Service.dto.ApiResponse;
import com.prunny.Task_Service.dto.TaskDTO;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.enums.TaskStatus;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.service.TaskProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/task_progress")
@RequiredArgsConstructor
public class TaskProgressController {

    private final TaskProgressService taskProgressService;

    @PostMapping("/{taskId}/{projectId}")
    public ResponseEntity<?> createNewTask(@PathVariable("taskId") Long taskId,
      @PathVariable("projectId")Long projectId,@RequestParam TaskStatus taskStatus) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        ApiResponse<TaskResponseDTO> response = ApiResponse.<TaskResponseDTO>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(taskProgressService.markTaskAsCompleted(taskId, projectId, taskStatus))
                .build();

        return ResponseEntity.ok(response);
    }
}
