package com.prunny.Task_Service.entity;


import com.prunny.Task_Service.enums.TaskPriority;
import com.prunny.Task_Service.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private String taskName;
    private String description;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime completionDate;
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;
    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;
    private boolean isOverdue;

    @ElementCollection
    private List<String> assignedUsers = new ArrayList<>();
    private Long projectId;


}
