package com.prunny.Report_Service.dto;

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
    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")
    private LocalDateTime endDate;
    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")
    private LocalDateTime dateCreated;
    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' h:mm a")
    private LocalDateTime dateUpdated;
}
