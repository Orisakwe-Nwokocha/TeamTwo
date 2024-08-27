package com.prunny.Task_Service.dto;

import com.prunny.Task_Service.enums.TaskPriority;
import com.prunny.Task_Service.enums.TaskStatus;
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
    private TaskStatus taskStatus;
    private TaskPriority taskPriority;
    private boolean hasOverdue;

   //
    private List<TaskUserDTO> assignedUsersDTO = new ArrayList<>();
    private Long createdByUserId;
    private Long projectId;
    /* end of relationships */

}
