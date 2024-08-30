package com.prunny.Report_Service.controller;

import com.prunny.Report_Service.dto.TaskResponseDTO;
import com.prunny.Report_Service.service.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {

    private final PdfGeneratorService pdfGeneratorService;

    @GetMapping("/downloadTaskReport/{taskId}/{projectId}")
    public ResponseEntity<byte[]> downloadTaskReport(
            @PathVariable("taskId") Long taskId,
            @PathVariable("projectId") Long projectId) {

        byte[] pdfReport = pdfGeneratorService.generateTaskReport(taskId, projectId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=task_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfReport);
    }
}
