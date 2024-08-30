package com.prunny.Task_Service.service;

import com.prunny.Task_Service.client.ProjectClient;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.dto.TaskUserDTO;
import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.repository.TaskRepository;
import com.prunny.Task_Service.serviceImpl.TaskAssignmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskAssignmentServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectClient projectClient;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskAssignmentServiceImpl taskAssignmentService;


    @Test
    void testAssignTaskToUsers() throws ResourceNotFoundException, ResourceAlreadyExistsException {
        // Arrange
        Long taskId = 1L;
        Long projectId = 1L;
        TaskUserDTO taskUserDTO = new TaskUserDTO();
        taskUserDTO.setUserEmail("test@example.com");

        Task task = new Task();
        task.setTaskId(taskId);
        task.setAssignedUserEmails(new ArrayList<>());

        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setTaskId(taskId);


        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
        when(projectClient.getProjectById(projectId)).thenReturn(responseEntity);


        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);


        when(modelMapper.map(task, TaskResponseDTO.class)).thenReturn(taskResponseDTO);


        TaskResponseDTO result = taskAssignmentService.assignTaskToUsers(taskId, projectId, taskUserDTO);


        assertNotNull(result);
        assertEquals(taskId, result.getTaskId());
        assertTrue(task.getAssignedUserEmails().contains("test@example.com"));

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(task);


        verify(projectClient, times(1)).getProjectById(projectId);
    }


    @Test
    void testAssignTaskToUsers_UserAlreadyAssigned() {

        ResponseEntity<Map<String, Object>> mockResponse = ResponseEntity.ok(Map.of("data", new Object()));
        when(projectClient.getProjectById(anyLong())).thenReturn(mockResponse);

        Task task = new Task();
        List<String> assignedEmails = new ArrayList<>();
        assignedEmails.add("user@example.com");
        task.setAssignedUserEmails(assignedEmails);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        TaskUserDTO taskUserDTO = new TaskUserDTO();
        taskUserDTO.setUserEmail("user@example.com");

        assertThrows(ResourceAlreadyExistsException.class, () ->
                taskAssignmentService.assignTaskToUsers(1L, 1L, taskUserDTO));

        verify(taskRepository, times(1)).findById(anyLong());
        verify(taskRepository, times(0)).save(any(Task.class)); // Should not save because of the exception
        verify(projectClient, times(1)).getProjectById(anyLong());
    }

    @Test
    void testAssignTaskToUsers_TaskNotFound() {

        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        TaskUserDTO taskUserDTO = new TaskUserDTO();
        taskUserDTO.setUserEmail("user@example.com");

        assertThrows(ResourceNotFoundException.class, () ->
                taskAssignmentService.assignTaskToUsers(1L, 1L, taskUserDTO));

        verify(taskRepository, times(1)).findById(anyLong());
        verify(taskRepository, times(0)).save(any(Task.class));
        verify(projectClient, times(0)).getProjectById(anyLong()); // Should not call ProjectClient because task was not found
    }
}
