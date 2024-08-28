package com.prunny.project_service.utils;

import com.prunny.project_service.dto.requests.CreateProjectRequest;

import java.util.Set;

import static java.time.LocalDateTime.now;

public class TestUtils {

    public static CreateProjectRequest buildCreateProjectRequest() {
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("New Project");
        request.setDescription("Project Description");
        request.setManager("Project Manager");
        request.setTeamMembers(Set.of("Member1", "Member2", "Member3"));
        request.setStartDate(now());
        request.setEndDate(now().plusDays(7));
        return request;
    }
}
