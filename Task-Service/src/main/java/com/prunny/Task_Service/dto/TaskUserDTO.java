package com.prunny.Task_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskUserDTO {

    private Long taskUserId;

    /* relationships */
    private TaskDTO taskDTO;
    private long userId;
    /* end of relationships */

}
