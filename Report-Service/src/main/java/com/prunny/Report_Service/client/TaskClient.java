package com.prunny.Report_Service.client;

import com.prunny.Report_Service.dto.ApiResponse;
import com.prunny.Report_Service.dto.TaskResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${task-service.name}", path = "/api/v1/task")
public interface TaskClient {

    @GetMapping("/get/{projectId}/{taskId}")
    ApiResponse<TaskResponseDTO> getTask(@PathVariable Long projectId, @PathVariable Long taskId);
}
