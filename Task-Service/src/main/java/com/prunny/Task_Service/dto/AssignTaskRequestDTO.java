package com.prunny.Task_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignTaskRequestDTO {

    private long taskId;
    private long projectId;
    private List<Long> userIdList;

}
