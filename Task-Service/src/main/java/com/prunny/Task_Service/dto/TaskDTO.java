package com.prunny.Task_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long taskId;

    private String taskName;
    private String description;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime completionDate;
    private String taskStatus;
    private String taskPriority;
    private boolean isOverdue;
    private List<String> assignedUsersDTO = new ArrayList<>();
    private Long projectId;


}
