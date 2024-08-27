package com.prunny.Task_Service.repository;


import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.enums.TaskPriority;
import com.prunny.Task_Service.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByAssignedUsers_UserIdAndTaskId(long userId, Long taskId);


    Optional<Task> findByProjectIdAndTaskName(Long projectId, String taskName);


    List<Task> findByProjectId(Long projectId);


    List<Task> findByTaskPriority(TaskPriority taskPriority);


    List<Task> findByTaskStatus(TaskStatus taskStatus);

}