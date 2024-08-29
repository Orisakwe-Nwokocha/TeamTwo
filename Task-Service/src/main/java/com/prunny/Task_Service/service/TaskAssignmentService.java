package com.prunny.Task_Service.service;

import com.prunny.Task_Service.dto.AssignTaskRequestDTO;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.dto.TaskUserDTO;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskAssignmentService {

    TaskResponseDTO assignTaskToUsers(Long taskId,Long projectId, TaskUserDTO taskUserDTO)
            throws ResourceNotFoundException, ResourceAlreadyExistsException;


}
