package com.prunny.Task_Service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private String manager;
    private Set<String> teamMembers;
    private Set<Long> taskIDs;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;

    private LocalDateTime responseTime;
}
