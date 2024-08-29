package com.prunny.Task_Service.serviceImpl;

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
import com.prunny.Task_Service.service.TaskManagementService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.prunny.Task_Service.enums.TaskStatus.TO_DO;

@Service
@Slf4j
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;
    ModelMapper modelMapper = new ModelMapper();

    public TaskManagementServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //Long projectId,
    @Override
    public TaskResponseDTO createNewTask( TaskRequestDTO taskRequest) throws ResourceNotFoundException, ResourceAlreadyExistsException {

        //find project by ID AND task name

        Task task = new Task();
        task.setTaskStatus(TO_DO);
        task.setTaskName(taskRequest.getTaskName());
        task.setDescription(taskRequest.getDescription());
        task.setTaskPriority(taskRequest.getTaskPriority());
        task.setDueDate(taskRequest.getDueDate());

        task = taskRepository.save(task);
        log.info("task successfully saved to the database");

        // Map the saved task to TaskResponseDTO
        return modelMapper.map(task, TaskResponseDTO.class);

        //project ID will give us necessary infos of project

    }

    @Override
    public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO taskRequest)
            throws ResourceNotFoundException, NotMemberOfProjectException, NotLeaderOfProjectException {


        Task taskToUpdate = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskToUpdate.setTaskStatus(taskRequest.getTaskStatus());
        taskToUpdate.setTaskName(taskRequest.getTaskName());
        taskToUpdate.setTaskPriority(taskRequest.getTaskPriority());
        taskToUpdate.setDueDate(taskRequest.getDueDate());
        taskToUpdate.setUpdatedAt(LocalDateTime.now());
        taskToUpdate.setDescription(taskRequest.getDescription());
        taskToUpdate.setCompletionDate(LocalDateTime.now());

        taskRepository.save(taskToUpdate);
        log.info("Edited task successfully saved to the database");

        return modelMapper.map(taskToUpdate, TaskResponseDTO.class);
    }

    @Override
    public TaskResponseDTO getTaskDetails(Long taskId) throws ResourceNotFoundException {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("task not found"));

        TaskResponseDTO taskResponse = modelMapper.map(task, TaskResponseDTO.class);
        log.info("Retrieved task details successfully");

        return taskResponse;
    }

   // long projectId,
    @Override
    public void deleteTask( long taskId) throws ResourceNotFoundException, NotLeaderOfProjectException {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("task not found"));

        taskRepository.delete(task);
        log.info("Task successfully deleted");

    }

    @Override
    public List<TaskResponseDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll(); // Adjust this if you have a specific query to get tasks

        // Convert the list of Task entities to a list of TaskResponseDTO
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDTO> searchTaskBasedOnDifferentCriteria(TaskStatus status, TaskPriority priority, Long projectId, Long assignedTo_UserId) {
        return List.of();
    }

    @Override
    public List<TaskDTO> getAllTasksForProject(Long projectId) throws ResourceNotFoundException {
        if (!taskRepository.existsByProjectId(projectId))
            throw new ResourceNotFoundException("Task does not exist for project");
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        log.info("Retrieved tasks successfully");
        return List.of(modelMapper.map(tasks, TaskDTO[].class));
    }


}
