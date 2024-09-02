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
import com.prunny.Report_Service.dto.ApiResponse;
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

import static java.time.LocalDateTime.now;

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
    public ApiResponse<String> generateAndUploadTaskReport(Long projectId, Long taskId) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Fetch project details
        ProjectDTO project = fetchProjectDetails(projectId);

        // Fetch task details
        TaskResponseDTO task = fetchTaskDetails(projectId, taskId);

        // Generate PDF document
        generateTaskReportPdf(byteArrayOutputStream, project, task);

        // Upload the PDF to Cloudinary and return the result
        return uploadTaskReportPdf(byteArrayOutputStream.toByteArray(), projectId, taskId);
    }

    private ProjectDTO fetchProjectDetails(Long projectId) {
        ProjectDTO project = projectClient.getTask(projectId).getData();
        log.info("Fetched project: {}", project);
        return project;
    }

    private TaskResponseDTO fetchTaskDetails(Long projectId, Long taskId) {
        TaskResponseDTO task = taskClient.getTask(projectId, taskId).getData();
        log.info("Fetched task: {}", task);
        return task;
    }

    private void generateTaskReportPdf(ByteArrayOutputStream byteArrayOutputStream, ProjectDTO project, TaskResponseDTO task) {
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        String teamMembers = String.join(", ", project.getTeamMembers());

        document.add(new Paragraph("Task Report").setBold().setFontSize(16));
        document.add(new Paragraph("Project: " + project.getName()));
        document.add(new Paragraph("Project Manager: " + project.getManager()));
        document.add(new Paragraph("Project Members: " + teamMembers));
        document.add(new Paragraph(" ")); // Adding a space for readability

        if (task != null) {
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3})).useAllAvailableWidth();
            populateTaskDetails(table, task);
            document.add(table);
        } else {
            document.add(new Paragraph("Task not found for this project."));
        }

        document.close();
    }

    private void populateTaskDetails(Table table, TaskResponseDTO task) {
        String members = String.join(", ", task.getAssignedUserEmails());
        addTableRow(table, "Task ID", String.valueOf(task.getTaskId()));
        addTableRow(table, "Task Name", task.getTaskName());
        addTableRow(table, "Description", task.getDescription());
        addTableRow(table, "Assigned Members", members);
        addTableRow(table, "Priority", task.getTaskPriority());
        addTableRow(table, "Status", task.getTaskStatus());
        addTableRow(table, "Overdue", String.valueOf(task.isOverdue()));
        addTableRow(table, "Created Date", format(task.getCreatedAt()));
        addTableRow(table, "Due Date", format(task.getDueDate()));
        addTableRow(table, "Completion Date", format(task.getCompletionDate()));
    }

    private ApiResponse<String> uploadTaskReportPdf(byte[] pdfBytes, Long projectId, Long taskId) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(pdfBytes, ObjectUtils.asMap(
                    "resource_type", "auto",
                    "public_id", "reports/project_" + projectId + "/task_" + taskId,
                    "overwrite", true
            ));
            String fileUrl = uploadResult.get("secure_url").toString();
            return ApiResponse.<String>builder()
                    .responseTime(now())
                    .success(true)
                    .message("Project task report generated successfully")
                    .data(fileUrl)
                    .build();
        } catch (IOException e) {
            String message = "Error uploading task report PDF";
            log.error("{}: {}", message, e.getMessage());
            throw new RuntimeException(message);
        }
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