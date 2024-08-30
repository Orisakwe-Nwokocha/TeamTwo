package com.prunny.Report_Service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {

    private Long taskId;
    private String taskName;

    private String description;
    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")

    private LocalDateTime dueDate;

    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' hh:mm a")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' hh:mm a")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' hh:mm a")
    private LocalDateTime completionDate;

    private boolean isOverdue;

    private String taskStatus;
//    private List<TaskUser> assignedUsers = new ArrayList<>();


    public TaskResponseDTO(Long taskId, String taskName, String description, LocalDateTime createdAt,
                           LocalDateTime completionDate, LocalDateTime dueDate, String taskStatus) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.createdAt = createdAt;
        this.completionDate = completionDate;
        this.dueDate = dueDate;
        this.taskStatus = taskStatus;
    }
}
