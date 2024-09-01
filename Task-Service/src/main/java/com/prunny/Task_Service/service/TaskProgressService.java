package com.prunny.Task_Service.service;

import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.enums.TaskStatus;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;

public interface TaskProgressService {

    TaskResponseDTO updateTaskProgress(Long taskId, Long projectId,String taskStatus) throws ResourceNotFoundException, ResourceAlreadyExistsException;
}
