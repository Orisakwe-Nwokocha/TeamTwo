package com.prunny.Report_Service.client;

import com.prunny.Report_Service.dto.ApiResponse;
import com.prunny.Report_Service.dto.ProjectDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service", path = "/projects")
public interface ProjectClient {

    @GetMapping("/{id}")
    ApiResponse<ProjectDTO> getTask(@PathVariable Long id);
}
