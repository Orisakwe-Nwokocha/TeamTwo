package com.prunny.Report_Service.serviceImpl;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.prunny.Report_Service.client.TaskClient;
import com.prunny.Report_Service.dto.TaskResponseDTO;
import com.prunny.Report_Service.service.PdfGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@Slf4j
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private final TaskClient taskClient;

    public PdfGeneratorServiceImpl(TaskClient taskClient) {
        this.taskClient = taskClient;
    }

    @Override
    public byte[] generateTaskReport(Long projectId, Long taskId) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Initialize PDF writer and document
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Add title and project/task information
        document.add(new Paragraph("Task Report").setBold().setFontSize(16));
        document.add(new Paragraph("Project ID: " + projectId));
        document.add(new Paragraph("Task ID: " + taskId));
        document.add(new Paragraph(" ")); // Adding a space for readability

        // Fetch task details
        TaskResponseDTO task = taskClient.getTaskDetails(projectId, taskId);

        log.info("Fetched task: {}", task);  // Log the fetched task

        // Check if task is found and populate PDF
        if (task != null) {
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3}))
                    .useAllAvailableWidth();

            // Add task details with null checks
            addTableRow(table, "Task ID", task.getTaskId() != null ? String.valueOf(task.getTaskId()) : "N/A");
            addTableRow(table, "Task Name", task.getTaskName());
            addTableRow(table, "Description", task.getDescription());
            addTableRow(table, "Created Date", task.getCreatedAt() != null ? task.getCreatedAt().toString() : "N/A");
            addTableRow(table, "Completion Date", task.getCompletionDate() != null ? task.getCompletionDate().toString() : "N/A");
            addTableRow(table, "Due Date", task.getDueDate() != null ? task.getDueDate().toString() : "N/A");
            addTableRow(table, "Status", task.getTaskStatus());

            document.add(table);
        } else {
            document.add(new Paragraph("Task not found for this project."));
        }

        // Close the document and return the PDF as a byte array
        document.close();
        return byteArrayOutputStream.toByteArray();
    }

    // Helper method to add table rows with null checks
    private void addTableRow(Table table, String header, String value) {
        table.addCell(header);
        table.addCell(value != null ? value : "N/A");
    }

}