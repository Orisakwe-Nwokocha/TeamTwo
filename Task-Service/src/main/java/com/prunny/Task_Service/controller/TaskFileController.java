package com.prunny.Task_Service.controller;

import com.prunny.Task_Service.dto.ApiResponse;
import com.prunny.Task_Service.dto.TaskFileDTO;
import com.prunny.Task_Service.dto.TaskRequestDTO;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.service.TaskFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskFileController {

    private final TaskFileService taskFileService;

    @PostMapping("/task_file/{taskId}/uploadFile")
    public ResponseEntity<?> uploadFile(@PathVariable("taskId") Long taskId, MultipartFile file, String fileName) throws ResourceAlreadyExistsException, ResourceNotFoundException, IOException {
        ApiResponse<TaskFileDTO> response = ApiResponse.<TaskFileDTO>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(taskFileService.uploadFile(taskId, file, fileName))
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/task_file/download/{fileId}")
    public ResponseEntity<?> getDownloadUrl(@PathVariable("fileId") String fileId) throws ResourceAlreadyExistsException, ResourceNotFoundException, IOException {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(taskFileService.getDownloadUrl(fileId))
                .build();

        return ResponseEntity.ok(response);
    }
}
