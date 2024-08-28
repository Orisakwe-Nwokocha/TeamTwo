package com.prunny.Task_Service.service;


import com.prunny.Task_Service.dto.TaskRequestDTO;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.enums.TaskPriority;
import com.prunny.Task_Service.enums.TaskStatus;
import com.prunny.Task_Service.exception.NotLeaderOfProjectException;
import com.prunny.Task_Service.exception.NotMemberOfProjectException;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;

import java.util.List;

public interface TaskManagementService {


    TaskResponseDTO createNewTask(Long projectId, TaskRequestDTO taskRequest)
            throws ResourceNotFoundException, ResourceAlreadyExistsException;


    TaskResponseDTO updateTask(Long taskId, TaskRequestDTO taskRequest)
            throws ResourceNotFoundException, NotMemberOfProjectException, NotLeaderOfProjectException;



    TaskResponseDTO getTaskDetails(Long taskId)
            throws ResourceNotFoundException;


    void deleteTask(long projectId, long taskId)
            throws ResourceNotFoundException, NotLeaderOfProjectException;



    List<TaskResponseDTO> searchTaskBasedOnDifferentCriteria(TaskStatus status, TaskPriority priority, Long projectId, Long assignedTo_UserId);

}
