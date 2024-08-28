package com.prunny.Task_Service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${project-service.name}", url = "${project-service.url}")
public interface ProjectClient {

    @GetMapping("/{id}")
     ResponseEntity<?> getProjectById(@PathVariable Long id);


}
