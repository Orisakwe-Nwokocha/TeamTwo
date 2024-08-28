package com.prunny.project_service.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AddTaskRequest {
    private Long projectId;
    @JsonProperty("name")
    private String taskName;
    private String description;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime dueDate;
    @JsonProperty("status")
    private String taskStatus;
    @JsonProperty("priority")
    private String taskPriority;
}
