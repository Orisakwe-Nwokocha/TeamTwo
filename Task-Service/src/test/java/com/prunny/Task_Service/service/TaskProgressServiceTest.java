package com.prunny.Task_Service.service;

import com.prunny.Task_Service.client.ProjectClient;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.enums.TaskStatus;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.repository.TaskRepository;
import com.prunny.Task_Service.serviceImpl.TaskProgressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskProgressServiceTest {


    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectClient projectClient;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskProgressServiceImpl taskService;

    private Task task;


    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTaskId(1L);
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        task.setDueDate(LocalDateTime.now().plusDays(1));
    }

    @Test
    void testUpdateTaskProgress_TaskNotFound() {

        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                taskService.updateTaskProgress(1L, 1L, TaskStatus.COMPLETED));
    }

    @Test
    void testUpdateTaskProgress_ProjectNotFound() {

        when(projectClient.getProjectById(anyLong())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                taskService.updateTaskProgress(1L, 1L, TaskStatus.COMPLETED));
    }

    @Test
    void testUpdateTaskProgress_TaskAlreadyCompleted() {

        task.setTaskStatus(TaskStatus.COMPLETED);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(projectClient.getProjectById(anyLong())).thenReturn(ResponseEntity.ok(Map.of("data", new Object())));

        assertThrows(ResourceAlreadyExistsException.class, () ->
                taskService.updateTaskProgress(1L, 1L, TaskStatus.COMPLETED));
    }

    @Test
    void testUpdateTaskProgress_Success() throws ResourceAlreadyExistsException, ResourceNotFoundException {

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(projectClient.getProjectById(anyLong())).thenReturn(ResponseEntity.ok(Map.of("data", new Object())));
        when(modelMapper.map(any(Task.class), eq(TaskResponseDTO.class))).thenReturn(new TaskResponseDTO());


        TaskResponseDTO responseDTO = taskService.updateTaskProgress(1L, 1L, TaskStatus.COMPLETED);


        assertNotNull(responseDTO);
        verify(taskRepository, times(1)).save(task);
    }

}
