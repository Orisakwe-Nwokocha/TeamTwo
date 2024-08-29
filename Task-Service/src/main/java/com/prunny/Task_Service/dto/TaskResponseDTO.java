package com.prunny.Task_Service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prunny.Task_Service.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
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

    private TaskStatus taskStatus;
//    private List<TaskUser> assignedUsers = new ArrayList<>();


}
