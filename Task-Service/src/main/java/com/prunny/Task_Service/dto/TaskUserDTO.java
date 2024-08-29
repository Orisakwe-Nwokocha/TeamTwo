package com.prunny.Task_Service.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskUserDTO {

    @Email
    private String userEmail;

}
//    private Long taskUserId;
//    private TaskDTO taskDTO;
//    private long userId;
