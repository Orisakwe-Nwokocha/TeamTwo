package com.prunny.Task_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskFileDTO {

    private String id;
    private String fileName;
    private String fileType;
    private long fileSize;
    private String fileUrl;
}
