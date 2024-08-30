package com.prunny.Task_Service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {

    private long commentId;
    private String text;
    private long userId;
    private long userTaskId;
    private long projectId;
    @JsonFormat(pattern = "EEEE',' dd-MMMM-yyyy 'at' hh:mm a")
    private String createdDate;

    private LocalDateTime updatedAt;
}
