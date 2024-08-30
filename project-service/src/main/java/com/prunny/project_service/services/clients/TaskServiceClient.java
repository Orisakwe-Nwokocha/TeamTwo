package com.prunny.project_service.services.clients;

import com.prunny.project_service.dto.requests.AssignTaskDTO;
import com.prunny.project_service.dto.responses.ApiResponse;
import com.prunny.project_service.services.clients.dto.requests.TaskRequestDTO;
import com.prunny.project_service.services.clients.dto.responses.TaskDTO;
import com.prunny.project_service.services.clients.dto.responses.TaskResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "task-service", path = "/api/v1/task")
public interface TaskServiceClient {

    @PostMapping("/createTask")
    ApiResponse<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequestDTO);

    @GetMapping("/getTasks")
    ApiResponse<List<TaskDTO>> getAllTasksForProject(@RequestParam Long projectId);

    @GetMapping("/get/{projectId}/{taskId}")
    ApiResponse<TaskResponseDTO> getProjectTask(@PathVariable Long projectId, @PathVariable Long taskId);

    @PostMapping("/assign_task/{taskId}/{projectId}")
    ApiResponse<TaskResponseDTO> assignTask(@PathVariable Long taskId,
                                            @PathVariable Long projectId,
                                            @RequestBody AssignTaskDTO request);
}
