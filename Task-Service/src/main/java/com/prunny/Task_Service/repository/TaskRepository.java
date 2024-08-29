package com.prunny.Task_Service.repository;


import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.enums.TaskPriority;
import com.prunny.Task_Service.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

//    Optional<Task> findByAssignedUsers_UserIdAndTaskId(long userId, Long taskId);
//
//

    List<Task> findByTaskStatusAndTaskPriority(TaskStatus status, TaskPriority priority);
    List<Task> findByTaskStatusAndProjectId(TaskStatus status, Long projectId);
    List<Task> findByTaskPriorityAndProjectId(TaskPriority priority, Long projectId);
    List<Task> findByTaskStatusAndTaskPriorityAndProjectId(TaskStatus status, TaskPriority priority, Long projectId);



    List<Task> findByTaskPriority(TaskPriority taskPriority);


    List<Task> findByTaskStatus(TaskStatus taskStatus);

    boolean existsByProjectId(Long projectId);

    List<Task> findByProjectId(Long projectId);
}