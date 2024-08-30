package com.prunny.Report_Service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class TaskResponseDTO {
    private Long projectId;

    private Long taskId;
    private String taskName;
    private String description;
    private String taskStatus;
    private String taskPriority;
    private boolean isOverdue;
    private Set<String> assignedUserEmails;

    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")
    private LocalDateTime dueDate;

    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")
    private LocalDateTime completionDate;
}
