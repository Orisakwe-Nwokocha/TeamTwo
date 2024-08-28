package com.prunny.Task_Service.serviceImpl;

import com.prunny.Task_Service.dto.TaskRequestDTO;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.enums.TaskPriority;
import com.prunny.Task_Service.enums.TaskStatus;
import com.prunny.Task_Service.exception.NotLeaderOfProjectException;
import com.prunny.Task_Service.exception.NotMemberOfProjectException;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.service.TaskManagementService;

import java.util.List;

public class TaskManagementServiceImpl implements TaskManagementService {


    @Override
    public TaskResponseDTO createNewTask(Long projectId, TaskRequestDTO taskRequest) throws ResourceNotFoundException, ResourceAlreadyExistsException {


    }

    @Override
    public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO taskRequest) throws ResourceNotFoundException, NotMemberOfProjectException, NotLeaderOfProjectException {
        return null;
    }

    @Override
    public TaskResponseDTO getTaskDetails(Long taskId) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public void deleteTask(long projectId, long taskId) throws ResourceNotFoundException, NotLeaderOfProjectException {

    }

    @Override
    public List<TaskResponseDTO> searchTaskBasedOnDifferentCriteria(TaskStatus status, TaskPriority priority, Long projectId, Long assignedTo_UserId) {
        return List.of();
    }


}
