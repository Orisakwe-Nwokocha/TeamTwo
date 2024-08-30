package com.prunny.Report_Service.service;

public interface PdfGeneratorService {

    byte[] generateTaskReport(Long projectId,Long taskId);
}
