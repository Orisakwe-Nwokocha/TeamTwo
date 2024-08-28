package com.prunny.project_service.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class CreateProjectRequest {
    private String name;
    private String description;
    private String manager;
    private Set<String> teamMembers;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime endDate;
}
