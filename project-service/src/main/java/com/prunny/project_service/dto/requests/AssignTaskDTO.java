package com.prunny.project_service.dto.requests;

import lombok.*;

import java.util.Set;

@Getter
@Setter
public class AssignTaskDTO {

    private Set<String> userEmails;

}
