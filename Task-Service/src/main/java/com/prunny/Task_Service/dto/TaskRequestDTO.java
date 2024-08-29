package com.prunny.Task_Service.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.prunny.Task_Service.enums.TaskPriority;
import com.prunny.Task_Service.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {

    private String taskName;
    private String description;
    @FutureOrPresent
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime dueDate;
    private TaskStatus taskStatus;
    private TaskPriority taskPriority;
    private Long projectId;
}
