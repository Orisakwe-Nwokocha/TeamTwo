package com.prunny.Report_Service.client;

import com.prunny.Report_Service.dto.ApiResponse;
import com.prunny.Report_Service.dto.TaskResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "${task-service.name}", url = "${task-service.url}")
public interface TaskClient {

    @GetMapping("get/{projectId}/{taskId}")
    TaskResponseDTO getTaskDetails(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId);
    //ApiResponse getTaskDetails(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId);

}
