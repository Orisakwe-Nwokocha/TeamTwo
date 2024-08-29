package com.prunny.Task_Service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class TaskFile {

    @Id
    private String id;
    private String fileName;
    private String fileType;
    private long fileSize;
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

}
