package com.prunny.Task_Service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDTO {

    private String taskName;
    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime dueDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completionDate;
    private String taskStatus;
    private String taskPriority;
    private boolean isOverdue;
    private List<String> assignedUsersDTO = new ArrayList<>();
    private Long projectId;


}
