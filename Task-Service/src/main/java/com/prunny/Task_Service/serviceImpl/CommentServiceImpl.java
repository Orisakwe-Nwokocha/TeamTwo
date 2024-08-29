package com.prunny.Task_Service.serviceImpl;

import com.prunny.Task_Service.client.ProjectClient;
import com.prunny.Task_Service.dto.CommentDto;
import com.prunny.Task_Service.dto.CommentResponseDto;
import com.prunny.Task_Service.entity.Comment;
import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.exception.MessagingException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.repository.CommentRepository;
import com.prunny.Task_Service.repository.TaskRepository;
import com.prunny.Task_Service.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ProjectClient projectClient;
    private final TaskRepository taskRepository;
    ModelMapper modelMapper = new ModelMapper();


    public CommentServiceImpl(CommentRepository commentRepository, ProjectClient projectClient, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.projectClient = projectClient;
        this.taskRepository = taskRepository;
    }

    @Override
    public CommentResponseDto commentOnTask(Long taskId, CommentDto commentDto) throws MessagingException, ResourceNotFoundException {

        //VALIDATION
        ResponseEntity<Map<String, Object>> response = projectClient.getProjectById(commentDto.getProjectId());


        if (response == null || response.getBody() == null) {
            throw new ResourceNotFoundException("PROJECT DOES NOT EXIST! CREATE THE PROJECT FIRST");
        }

        // Find the task by ID
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isEmpty()) {
            throw new MessagingException("Task not found");
        }

        // Validate that the comment text is not empty
        if (commentDto.getText().trim().isEmpty()) {
            throw new MessagingException("Comment cannot be empty");
        }

        Task task = taskOptional.get();


        // Create and save the comment
        Comment comment = new Comment();
        comment.setProjectId(commentDto.getProjectId());
        comment.setText(commentDto.getText());
      //  comment.setUserId(commentDto.getUserId());
        comment.setTask(task);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        return modelMapper.map(comment, CommentResponseDto.class);
    }


    @Override
    public CommentResponseDto getCommentById(long id) throws MessagingException {
        Optional<Comment> commentOptional = commentRepository.findById(id);

        if (commentOptional.isEmpty()) {
            throw new MessagingException("Comment not found");
        }

        Comment comment = commentOptional.get();
        return modelMapper.map(comment, CommentResponseDto.class);
    }

    @Override
    public List<CommentResponseDto> getCommentsByProject(Long projectId) throws MessagingException {
        List<Comment> comments = commentRepository.findByProjectId(projectId);

        if (comments.isEmpty()) {
            throw new MessagingException("No comments found for this project");
        }

        // Map the list of comments to a list of CommentResponseDto
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentResponseDto.class))
                .collect(Collectors.toList());
    }

        @Override
        public List<CommentResponseDto> getCommentsByTaskId(Long taskId) throws MessagingException {
            // Validate if the task exists
            if (!taskRepository.existsById(taskId)) {
                throw new MessagingException("Task not found with the specified ID");
            }

            // Fetch comments for the given task
            List<Comment> comments = commentRepository.findByTask_TaskId(taskId);
            if (comments.isEmpty()) {
                throw new MessagingException("No comments found for this task");
            }

            // Convert entities to DTOs
            return comments.stream()
                    .map(comment -> modelMapper.map(comment, CommentResponseDto.class))
                    .collect(Collectors.toList());
        }



    //, String authentication
    @Override
    public CommentResponseDto updateComment(Long id, String text) throws MessagingException {

        Optional<Comment> commentOptional = commentRepository.findById(id);

        if (commentOptional.isEmpty()) {
            throw new MessagingException("Comment not found");
        }

        Comment comment = commentOptional.get();

//        if (!comment.getUserId().equals(getUserIdFromAuthentication(authentication))) {
//            throw new MessagingException("You are not authorized to update this comment");
//        }

        // Validate that the new text is not empty
        if (text.trim().isEmpty()) {
            throw new MessagingException("Comment text cannot be empty");
        }

        // Update the comment's text and save it
        comment.setText(text);
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        // Map and return the updated comment
        return modelMapper.map(comment, CommentResponseDto.class);
    }



    //, String authorization
    @Override
    public void deleteComment(Long id) throws MessagingException {

        Optional<Comment> commentOptional = commentRepository.findById(id);

        if (commentOptional.isEmpty()) {
            throw new MessagingException("Comment not found");
        }

        Comment comment = commentOptional.get();

        // Check if the authenticated user is the owner of the comment

        // Delete the comment
        commentRepository.deleteById(id);
    }



}
