package com.prunny.project_service.services;

import com.prunny.project_service.dto.requests.CreateProjectRequest;
import com.prunny.project_service.dto.responses.ApiResponse;
import com.prunny.project_service.dto.responses.ProjectDTO;
import com.prunny.project_service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static com.prunny.project_service.utils.TestUtils.buildCreateProjectRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

    @Test
    void testCreateProject() {
        CreateProjectRequest request = buildCreateProjectRequest();
        ApiResponse<ProjectDTO> response = projectService.createProject(request);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getManager()).isNotNull();
    }

    @Test
    void testCreateProjectWithoutManager_throwsException() {
        CreateProjectRequest request = buildCreateProjectRequest();
        request.setManager(null);
        assertThatThrownBy(() -> projectService.createProject(request))
                            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testFindProjectById() {
        ApiResponse<ProjectDTO> response = projectService.getProjectBy(1L);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getId()).isEqualTo(1L);
    }

    @Test
    void testFindProjectByNonExistingId_throwsException() {
        assertThatThrownBy(() -> projectService.getProjectBy(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

}