package com.prunny.Report_Service;

import com.prunny.Report_Service.client.TaskClient;
import com.prunny.Report_Service.dto.ApiResponse;
import com.prunny.Report_Service.dto.TaskResponseDTO;
import com.prunny.Report_Service.serviceImpl.PdfGeneratorServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PdfGeneratorServiceTest {
     @MockBean
    private PdfGeneratorServiceImpl pdfGeneratorService;

    @Test
    public void testGenerateTaskReport() {
        // Create a mock of TaskResponseDTO
        TaskResponseDTO mockTaskResponseDTO = Mockito.mock(TaskResponseDTO.class);

        // Define the behavior of the mock
        Mockito.when(mockTaskResponseDTO.getTaskId()).thenReturn(1L);
        Mockito.when(mockTaskResponseDTO.getTaskName()).thenReturn("Sample Task");
        Mockito.when(mockTaskResponseDTO.getDescription()).thenReturn("This is a sample task");
        Mockito.when(mockTaskResponseDTO.getCreatedAt()).thenReturn(LocalDateTime.now());
        Mockito.when(mockTaskResponseDTO.getCompletionDate()).thenReturn(LocalDateTime.now().plusDays(1));
        Mockito.when(mockTaskResponseDTO.getDueDate()).thenReturn(LocalDateTime.now().plusDays(2));
        Mockito.when(mockTaskResponseDTO.getTaskStatus()).thenReturn("IN_PROGRESS");

        TaskClient mockTaskClient = Mockito.mock(TaskClient.class);
        Mockito.when(mockTaskClient.getTask(Mockito.anyLong(), Mockito.anyLong()).getData())
                .thenReturn(mockTaskResponseDTO);


        ApiResponse<String> pdfReport = pdfGeneratorService.generateAndUploadTaskReport(1L, 1L);

        // Validate the PDF content
        assertNotNull(pdfReport);
    }

}
