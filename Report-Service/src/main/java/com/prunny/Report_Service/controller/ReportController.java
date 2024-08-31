package com.prunny.Report_Service.controller;

import com.prunny.Report_Service.dto.ApiResponse;
import com.prunny.Report_Service.service.PdfGeneratorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final PdfGeneratorService pdfGeneratorService;

    @GetMapping("/download/{taskId}/{projectId}")
    public ResponseEntity<?> downloadTaskReport(
            @PathVariable("taskId") Long taskId,
            @PathVariable("projectId") Long projectId,
            HttpServletRequest request) {

        log.info("Downloading report for task: {}, project: {}", taskId, projectId);
        try {
            return ResponseEntity.ok(pdfGeneratorService.generateAndUploadTaskReport(projectId, taskId));
        } catch (Exception exception) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<String>builder()
                            .responseTime(now())
                            .success(false)
                            .error("PDF generation failed")
                            .message("Failed to generate or upload the task report")
                            .data(exception.getMessage())
                            .path(request.getRequestURI())
                            .build());
        }

    }

}
