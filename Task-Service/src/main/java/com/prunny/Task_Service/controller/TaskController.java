package com.prunny.Task_Service.controller;

import com.prunny.Task_Service.dto.ApiResponse;
import com.prunny.Task_Service.dto.TaskDTO;
import com.prunny.Task_Service.dto.TaskRequestDTO;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.enums.TaskPriority;
import com.prunny.Task_Service.enums.TaskStatus;
import com.prunny.Task_Service.exception.NotLeaderOfProjectException;
import com.prunny.Task_Service.exception.NotMemberOfProjectException;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.service.TaskManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskManagementService taskManagementService;


    @PostMapping("/createTask")
    public ResponseEntity<?> createNewTask( @RequestBody TaskDTO taskRequestDTO) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        ApiResponse<TaskResponseDTO> response = ApiResponse.<TaskResponseDTO>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(taskManagementService.createNewTask(taskRequestDTO)) //projectId,
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateTask/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable("taskId") Long taskId, @RequestBody TaskDTO taskRequestDTO) throws ResourceNotFoundException, NotMemberOfProjectException, NotLeaderOfProjectException {

        ApiResponse<TaskResponseDTO> response = ApiResponse.<TaskResponseDTO>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(taskManagementService.updateTask(taskId,taskRequestDTO))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("get/{projectId}/{taskId}")
    public ResponseEntity<?> getTaskDetails(@PathVariable("projectId") Long projectId,@PathVariable("taskId") Long taskId) throws ResourceNotFoundException, NotMemberOfProjectException, NotLeaderOfProjectException {

        ApiResponse<TaskResponseDTO> response = ApiResponse.<TaskResponseDTO>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(taskManagementService.getTaskDetails(projectId,taskId))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllTask")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getAllTask() {


        List<TaskResponseDTO> tasks = taskManagementService.getAllTasks();


        ApiResponse<List<TaskResponseDTO>> response = ApiResponse.<List<TaskResponseDTO>>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(tasks)
                .build();

        // Return the response wrapped in ResponseEntity
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("delete/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") Long taskId) throws ResourceNotFoundException, NotMemberOfProjectException, NotLeaderOfProjectException {

        return ResponseEntity.ok("Task successfully deleted");
    }


    @GetMapping("/getTasks")
    public ResponseEntity<?> getAllTasksForProject(@RequestParam Long projectId) throws ResourceNotFoundException {
        List<TaskDTO> tasksForProject = taskManagementService.getAllTasksForProject(projectId);
        ApiResponse<List<TaskDTO>> response = ApiResponse.<List<TaskDTO>>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .message("Tasks retrieved successfully")
                .data(tasksForProject)
                .build();
        return ResponseEntity.ok(response);
    }



    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> searchTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) Long projectId) {

        List<TaskResponseDTO> tasks = taskManagementService.searchTaskBasedOnDifferentCriteria(status, priority, projectId);

        ApiResponse<List<TaskResponseDTO>> response = ApiResponse.<List<TaskResponseDTO>>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(tasks)
                .build();

        return ResponseEntity.ok(response);
    }

}
