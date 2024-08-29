package com.prunny.Task_Service.controller;

import com.prunny.Task_Service.dto.*;
import com.prunny.Task_Service.exception.MessagingException;
import com.prunny.Task_Service.exception.ResourceAlreadyExistsException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment_on_task/{taskId}")
    public ResponseEntity<?> commentOnTask(@PathVariable("taskId") Long taskId, @RequestBody CommentDto commentDto) throws ResourceAlreadyExistsException, ResourceNotFoundException, MessagingException {
        ApiResponse<CommentResponseDto> response = ApiResponse.<CommentResponseDto>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(commentService.commentOnTask(taskId, commentDto)) //projectId,
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/get_Comment/{taskId}")
    public ResponseEntity<?> getCommentByTaskId(@PathVariable("taskId") Long taskId) throws ResourceAlreadyExistsException, ResourceNotFoundException, MessagingException {
        ApiResponse<CommentResponseDto> response = ApiResponse.<CommentResponseDto>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(commentService.getCommentById(taskId)) //projectId,
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/get_Comment_project/{projectId}")
    public ResponseEntity<?> getCommentsByProjectId(@PathVariable("projectId") Long projectId) throws ResourceAlreadyExistsException, ResourceNotFoundException, MessagingException {
        ApiResponse<List<CommentResponseDto>> response = ApiResponse.<List<CommentResponseDto>>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(commentService.getCommentsByProject(projectId)) //projectId,
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/get_Comment_task/{taskId}")
    public ResponseEntity<?> getCommentsByTaskId(@PathVariable("taskId") Long taskId) throws ResourceAlreadyExistsException, ResourceNotFoundException, MessagingException {
        List<CommentResponseDto> comments = commentService.getCommentsByTaskId(taskId);

        ApiResponse<List<CommentResponseDto>> response = ApiResponse.<List<CommentResponseDto>>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(comments)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update_Comment/{id}")
    public ResponseEntity<?> updateComments(@PathVariable("id") Long id, @RequestBody String text) throws ResourceAlreadyExistsException, ResourceNotFoundException, MessagingException {

        ApiResponse<CommentResponseDto> response = ApiResponse.<CommentResponseDto>builder()
                .responseTime(LocalDateTime.now())
                .success(true)
                .data(commentService.updateComment(id,text)) //projectId,
                .build();

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) throws MessagingException {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }


}
