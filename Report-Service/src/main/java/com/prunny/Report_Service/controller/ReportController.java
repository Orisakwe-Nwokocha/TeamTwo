package com.prunny.Report_Service.controller;

import com.itextpdf.io.exceptions.IOException;
import com.prunny.Report_Service.service.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {

    private final PdfGeneratorService pdfGeneratorService;

    @GetMapping("/downloadTaskReport/{taskId}/{projectId}")
    public ResponseEntity<?> downloadTaskReport(
            @PathVariable("taskId") Long taskId,
            @PathVariable("projectId") Long projectId) {

        try {

            String fileUrl = pdfGeneratorService.generateAndUploadTaskReport(taskId, projectId);
            return ResponseEntity.ok("User Task Report: " + fileUrl); // Return the Cloudinary URL
        } catch (IOException | java.io.IOException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to generate or upload the task report.");
        }
    }

}
