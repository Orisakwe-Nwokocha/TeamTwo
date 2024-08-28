package com.prunny.project_service.services.clients.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class TaskResponseDTO {

    private Long taskId;
    private String taskName;
    private String description;
    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")
    private LocalDateTime dueDate;
    private String projectName;
    private String taskStatus;
    private Map<String, String> assignedUsers;

}
