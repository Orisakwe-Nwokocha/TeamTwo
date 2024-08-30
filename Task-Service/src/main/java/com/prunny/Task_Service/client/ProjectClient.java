package com.prunny.Task_Service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "project-service", path = "/projects")
public interface ProjectClient {

    @GetMapping("/{id}")
    ResponseEntity<Map<String, Object>> getProjectById(@PathVariable Long id);



}
