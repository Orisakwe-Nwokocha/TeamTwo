package com.prunny.Task_Service.service;


import com.prunny.Task_Service.dto.TaskDTO;
import com.prunny.Task_Service.dto.TaskRequestDTO;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.enums.TaskPriority;
import com.prunny.Task_Service.enums.TaskStatus;
import com.prunny.Task_Service.exception.NotLeaderOfProjectException;
import com.prunny.Task_Service.exception.NotMemberOfProjectException;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskManagementService {


   TaskResponseDTO createNewTask(TaskDTO taskRequest)
            throws ResourceNotFoundException, ResourceAlreadyExistsException;


    TaskResponseDTO updateTask(Long taskId, TaskDTO taskRequest)
            throws ResourceNotFoundException, NotMemberOfProjectException, NotLeaderOfProjectException;



    TaskResponseDTO getTaskDetails(Long projectId, Long taskId)
            throws ResourceNotFoundException;


    //long projectId,
    void deleteTask(Long projectId, long taskId)
            throws ResourceNotFoundException, NotLeaderOfProjectException;


    List<TaskResponseDTO> getAllTasks();


    List<TaskResponseDTO> searchTaskBasedOnDifferentCriteria(TaskStatus status, TaskPriority priority, Long projectId);

}
