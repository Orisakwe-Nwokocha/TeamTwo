package com.prunny.project_service.services.clients;

import com.prunny.project_service.dto.responses.ApiResponse;
import com.prunny.project_service.services.clients.dto.requests.TaskRequestDTO;
import com.prunny.project_service.services.clients.dto.responses.TaskDTO;
import com.prunny.project_service.services.clients.dto.responses.TaskResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "task-service", path = "/api/v1/task")
public interface TaskServiceClient {

    @PostMapping("/createTask")
    ApiResponse<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequestDTO);
    @GetMapping("/getTasks")
    ApiResponse<List<TaskDTO>> getAllTasksForProject(@RequestParam Long projectId);
}
