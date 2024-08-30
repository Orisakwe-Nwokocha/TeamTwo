package com.prunny.Task_Service.service;

import com.cloudinary.Uploader;
import com.prunny.Task_Service.dto.TaskFileDTO;
import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.entity.TaskFile;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.repository.TaskFileRepository;
import com.prunny.Task_Service.repository.TaskRepository;
import com.prunny.Task_Service.serviceImpl.TaskFileServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskFileServiceTest {

    @Mock
    private TaskFileRepository taskFileRepository;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskFileServiceImpl taskFileService;

    private Task task;
    private TaskFile taskFile;
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTaskId(1L);

        taskFile = new TaskFile();
        taskFile.setId(UUID.randomUUID().toString());
        taskFile.setFileUrl("http://example.com/file.png");

        multipartFile = mock(MultipartFile.class);
    }

    @Test
    void testUploadFile_TaskNotFound() {

        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () ->
                taskFileService.uploadFile(1L, multipartFile, "file.png"));
    }

   //UPLOAD FILE IS DIRECTLY BE LINKED TO CLOUDINARY


    @Test
    void testGetDownloadUrl_FileNotFound() {

        when(taskFileRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                taskFileService.getDownloadUrl(UUID.randomUUID().toString()));
    }

    @Test
    void testGetDownloadUrl_Success() throws ResourceNotFoundException {

        when(taskFileRepository.findById(anyString())).thenReturn(Optional.of(taskFile));

        String downloadUrl = taskFileService.getDownloadUrl(taskFile.getId());

        assertEquals(taskFile.getFileUrl(), downloadUrl);
    }
}
