package com.prunny.Report_Service.service;

import java.io.IOException;

public interface PdfGeneratorService {

    String generateAndUploadTaskReport(Long projectId,Long taskId) throws IOException;
}
