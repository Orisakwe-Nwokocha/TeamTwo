package com.prunny.Task_Service.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.prunny.Task_Service.dto.TaskFileDTO;
import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.entity.TaskFile;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.repository.TaskFileRepository;
import com.prunny.Task_Service.repository.TaskRepository;
import com.prunny.Task_Service.service.TaskFileService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class TaskFileServiceImpl implements TaskFileService {

   private final TaskFileRepository taskFileRepository;
   private final Cloudinary cloudinary;
   private final ModelMapper modelMapper;
   private final TaskRepository taskRepository;

    public TaskFileServiceImpl(TaskFileRepository taskFileRepository, Cloudinary cloudinary, ModelMapper modelMapper, TaskRepository taskRepository) {
        this.taskFileRepository = taskFileRepository;
        this.cloudinary = cloudinary;
        this.modelMapper = modelMapper;
        this.taskRepository = taskRepository;
    }


    @Override
    public TaskFileDTO uploadFile(Long taskId, MultipartFile file, String fileName) throws IOException, ResourceNotFoundException {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String fileUrl = uploadResult.get("secure_url").toString();

        String uniqueId = UUID.randomUUID().toString();

        TaskFile fileEntity = new TaskFile();
        fileEntity.setId(uniqueId);
        fileEntity.setFileUrl(fileUrl);
        fileEntity.setFileName(fileName);
        fileEntity.setFileSize(file.getSize());
        fileEntity.setFileType(file.getContentType());
        taskFileRepository.save(fileEntity);
        log.info("File successfully uploaded");

        return  modelMapper.map(fileEntity,TaskFileDTO.class);
    }

    //maybe user validation for only assign users to the task
    @Override
    public String getDownloadUrl(String fileId) throws ResourceNotFoundException {

        TaskFile taskFile = taskFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));

        return taskFile.getFileUrl();
    }

}
