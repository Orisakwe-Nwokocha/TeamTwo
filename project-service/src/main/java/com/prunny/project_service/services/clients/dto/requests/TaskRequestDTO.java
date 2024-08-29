package com.prunny.project_service.services.clients.dto.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskRequestDTO {
    private String taskName;
    private String description;
    private LocalDateTime dueDate;
    private String taskStatus;
    private String taskPriority;
    private Long projectId;
}
