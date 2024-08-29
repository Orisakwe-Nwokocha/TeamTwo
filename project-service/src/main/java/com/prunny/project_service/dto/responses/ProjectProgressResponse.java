package com.prunny.project_service.dto.responses;

import com.prunny.project_service.data.models.Project;
import com.prunny.project_service.services.clients.dto.responses.TaskDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProjectProgressResponse {
    private Project project;
    private List<TaskDTO> tasks = new ArrayList<>();
    private String overallProgress;
}
