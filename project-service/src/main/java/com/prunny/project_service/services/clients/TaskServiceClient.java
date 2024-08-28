package com.prunny.project_service.services.clients;

import com.prunny.project_service.dto.responses.ApiResponse;
import com.prunny.project_service.services.clients.dto.requests.TaskRequestDTO;
import com.prunny.project_service.services.clients.dto.responses.TaskResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "task-service", path = "/api/v1/task")
public interface TaskServiceClient {

    @PostMapping("/createTask")
    ApiResponse<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequestDTO);
}
