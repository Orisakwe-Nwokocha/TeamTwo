package com.prunny.Task_Service.service;


import com.prunny.Task_Service.dto.TaskFileDTO;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TaskFileService {

    TaskFileDTO uploadFile(Long taskId, MultipartFile file, String fileName) throws IOException, ResourceNotFoundException;

    String getDownloadUrl(String fileId) throws ResourceNotFoundException;

}