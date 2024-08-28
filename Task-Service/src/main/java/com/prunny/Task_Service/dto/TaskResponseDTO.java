package com.prunny.Task_Service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prunny.Task_Service.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {

    private long taskId;
    private String taskName;

    private LocalDateTime dueDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completionDate;

    private boolean isOverdue;

    private TaskStatus taskStatus;
    private Map<String, String> assignedUsers;

    private List<ProjectDTO> projectDTO;

}
