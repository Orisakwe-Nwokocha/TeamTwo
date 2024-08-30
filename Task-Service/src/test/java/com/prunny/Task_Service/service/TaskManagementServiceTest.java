package com.prunny.Task_Service.service;


import com.prunny.Task_Service.client.ProjectClient;
import com.prunny.Task_Service.dto.ProjectDTO;
import com.prunny.Task_Service.dto.TaskDTO;
import com.prunny.Task_Service.dto.TaskRequestDTO;
import com.prunny.Task_Service.dto.TaskResponseDTO;
import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.enums.TaskPriority;
import com.prunny.Task_Service.enums.TaskStatus;
import com.prunny.Task_Service.exception.NotLeaderOfProjectException;
import com.prunny.Task_Service.exception.NotMemberOfProjectException;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.repository.TaskRepository;
import com.prunny.Task_Service.serviceImpl.TaskManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskManagementServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectClient projectClient;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskManagementServiceImpl taskManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskManagementService = new TaskManagementServiceImpl(taskRepository, projectClient);

    }


    @Test
    public void testCreateNewTask() throws Exception {
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        taskRequest.setTaskName("Test Task");
        taskRequest.setDescription("Test Description");
        taskRequest.setTaskPriority(TaskPriority.valueOf("HIGH"));
        taskRequest.setDueDate(LocalDateTime.now().plusDays(5));
        taskRequest.setProjectId(1L);

        Task task = new Task();
        task.setTaskName("Test Task");
        task.setDescription("Test Description");
        task.setTaskPriority(TaskPriority.HIGH);
        task.setDueDate(LocalDateTime.now().plusDays(5));
        task.setProjectId(1L);

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(projectClient.getProjectById(anyLong())).thenReturn(ResponseEntity.ok(Collections.singletonMap("data", Collections.singletonMap("id", 1L))));

        TaskResponseDTO response = taskManagementService.createNewTask(taskRequest);

        assertNotNull(response);
        assertEquals("Test Task", response.getTaskName());
        verify(taskRepository, times(1)).save(any(Task.class));

    }

    @Test
    public void testGetTaskDetails() throws Exception {
        Task task = new Task();
        task.setTaskId(1L);
        task.setTaskName("Test Task");

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(projectClient.getProjectById(anyLong())).thenReturn(ResponseEntity.ok(Collections.singletonMap("data", Collections.singletonMap("id", 1L))));

        TaskResponseDTO response = taskManagementService.getTaskDetails(1L, 1L);

        assertNotNull(response);
        assertEquals("Test Task", response.getTaskName());
        verify(taskRepository, times(1)).findById(anyLong());
        verify(projectClient, times(1)).getProjectById(anyLong());
    }

    @Test
    public void testDeleteTask() throws Exception {
        Task task = new Task();
        task.setTaskId(1L);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(any(Task.class));
        when(projectClient.getProjectById(anyLong())).thenReturn(ResponseEntity.ok(Collections.singletonMap("data", Collections.singletonMap("id", 1L))));

        assertDoesNotThrow(() -> taskManagementService.deleteTask(1L, 1L));

        verify(taskRepository, times(1)).findById(anyLong());
        verify(taskRepository, times(1)).delete(any(Task.class));
        verify(projectClient, times(1)).getProjectById(anyLong());
    }

    @Test
    public void testGetAllTasks() {
        List<Task> tasks = Arrays.asList(new Task(), new Task());

        when(taskRepository.findAll()).thenReturn(tasks);

        List<TaskResponseDTO> response = taskManagementService.getAllTasks();

        assertNotNull(response);
        assertEquals(2, response.size());
        verify(taskRepository, times(1)).findAll();
    }


    @Test
    public void testUpdateTask() throws Exception {
        TaskRequestDTO taskDTO = new TaskRequestDTO();
        taskDTO.setTaskName("Updated Task");
        taskDTO.setDescription("Updated Description");
        taskDTO.setTaskPriority(TaskPriority.valueOf("MEDIUM"));
        taskDTO.setDueDate(LocalDateTime.now().plusDays(10));
        taskDTO.setProjectId(1L);

        Task existingTask = new Task();
        existingTask.setTaskId(1L);
        existingTask.setTaskName("Old Task");

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(projectClient.getProjectById(anyLong())).thenReturn(ResponseEntity.ok(Collections.singletonMap("data", Collections.singletonMap("id", 1L))));

        TaskResponseDTO response = taskManagementService.updateTask(1L, taskDTO);

        assertNotNull(response);
        assertEquals("Updated Task", response.getTaskName());
        verify(taskRepository, times(1)).findById(anyLong());
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(projectClient, times(1)).getProjectById(anyLong());
    }

    @Test
    void getTaskDetails_taskNotFound() {

        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskManagementService.getTaskDetails(1L, 1L);
        });
    }

    @Test
    public void testSearchTaskBasedOnDifferentCriteria() {
        List<Task> tasks = Arrays.asList(new Task(), new Task());

        when(taskRepository.findByTaskStatusAndTaskPriorityAndProjectId(any(), any(), anyLong())).thenReturn(tasks);

        List<TaskResponseDTO> response = taskManagementService.searchTaskBasedOnDifferentCriteria(TaskStatus.TO_DO, TaskPriority.HIGH, 1L);

        assertNotNull(response);
        assertEquals(2, response.size());
        verify(taskRepository, times(1)).findByTaskStatusAndTaskPriorityAndProjectId(any(), any(), anyLong());
    }


    @Test
    public void testGetAllTasksForProject() throws Exception {
        List<Task> tasks = Arrays.asList(new Task(), new Task());

        when(taskRepository.findByProjectId(anyLong())).thenReturn(tasks);
        when(taskRepository.existsByProjectId(anyLong())).thenReturn(true);

        List<TaskDTO> response = taskManagementService.getAllTasksForProject(1L);

        assertNotNull(response);
        assertEquals(2, response.size());
        verify(taskRepository, times(1)).findByProjectId(anyLong());
        verify(taskRepository, times(1)).existsByProjectId(anyLong());
    }
}

