package com.prunny.Task_Service.service;

import com.prunny.Task_Service.dto.AssignTaskDTO;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface TaskAssignmentService {

    TaskResponseDTO assignTaskToUsers(Long taskId, Long projectId, AssignTaskDTO requestDTO)
            throws ResourceNotFoundException, ResourceAlreadyExistsException;


}
