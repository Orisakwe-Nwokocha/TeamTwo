package com.prunny.Report_Service.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.prunny.Report_Service.client.ProjectClient;
import com.prunny.Report_Service.client.TaskClient;
import com.prunny.Report_Service.dto.ProjectDTO;
import com.prunny.Report_Service.dto.TaskResponseDTO;
import com.prunny.Report_Service.service.PdfGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private final TaskClient taskClient;
    private final ProjectClient projectClient;
    private final Cloudinary cloudinary;


    public PdfGeneratorServiceImpl(TaskClient taskClient, ProjectClient projectClient, Cloudinary cloudinary) {
        this.taskClient = taskClient;
        this.projectClient = projectClient;
        this.cloudinary = cloudinary;
    }

    @Override
    public String generateAndUploadTaskReport(Long projectId, Long taskId) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Fetch project details
        ProjectDTO project = projectClient.getTask(projectId).getData();
        log.info("Fetched project: {}", project);  // Log the fetched project

        // Fetch task details
        TaskResponseDTO task = taskClient.getTask(projectId, taskId).getData();
        log.info("Fetched task: {}", task);  // Log the fetched task

      //  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Initialize PDF writer and document
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Add title and project/task information
        document.add(new Paragraph("Task Report").setBold().setFontSize(16));
        document.add(new Paragraph("Project: " + project.getName()));
        document.add(new Paragraph("Project Manager: " + project.getManager()));
        document.add(new Paragraph("Project Members: " + project.getTeamMembers()));
        document.add(new Paragraph(" ")); // Adding a space for readability

        // Check if task is found and populate PDF
        if (task != null) {
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3}))
                    .useAllAvailableWidth();

            // Add task details with null checks
            addTableRow(table, "Task ID", String.valueOf(task.getTaskId()));
            addTableRow(table, "Task Name", task.getTaskName());
            addTableRow(table, "Description", task.getDescription());
            addTableRow(table, "Assigned Members", String.valueOf(task.getAssignedUserEmails()));
            addTableRow(table, "Priority", task.getTaskPriority());
            addTableRow(table, "Status", task.getTaskStatus());
            addTableRow(table, "Overdue", String.valueOf(task.isOverdue()));
            addTableRow(table, "Created Date", format(task.getCreatedAt()));
            addTableRow(table, "Due Date", format(task.getDueDate()));
            addTableRow(table, "Completion Date", format(task.getCompletionDate()));

            document.add(table);
        } else {
            document.add(new Paragraph("Task not found for this project."));
        }

        // Close the document and return the PDF as a byte array
        document.close();

       // return byteArrayOutputStream.toByteArray();

        // Upload the PDF to Cloudinary
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        Map<?, ?> uploadResult = cloudinary.uploader().upload(pdfBytes, ObjectUtils.asMap(
                "resource_type", "raw",
                "public_id", "task_reports/" + taskId + "_report",
                "overwrite", true
        ));

        // Return the URL for download
        String fileUrl = uploadResult.get("secure_url").toString();
        return fileUrl;
    }

    // Helper method to add table rows with null checks
    private void addTableRow(Table table, String header, String value) {
        table.addCell(header);
        table.addCell(!Objects.equals(value, "null") ? value : "N/A");
    }

    private static String format(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE',' dd-MMMM-yyyy 'at' h:mm a");
        return localDateTime.format(formatter);
    }

}