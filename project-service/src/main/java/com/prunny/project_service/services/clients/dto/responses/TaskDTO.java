package com.prunny.project_service.services.clients.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class TaskDTO {

    private Long projectId;

    private Long taskId;
    private String taskName;
    private String description;
    private String taskStatus;
    private String taskPriority;
    private boolean isOverdue;
    private List<String> assignedUsersDTO;

    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")
    private LocalDateTime dueDate;

    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")
    private LocalDateTime completionDate;

}
