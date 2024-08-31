package com.prunny.Report_Service.service;

import com.prunny.Report_Service.dto.ApiResponse;

public interface PdfGeneratorService {

    ApiResponse<String> generateAndUploadTaskReport(Long projectId, Long taskId);
}
