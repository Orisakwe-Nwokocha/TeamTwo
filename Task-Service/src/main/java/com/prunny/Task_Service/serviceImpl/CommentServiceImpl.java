package com.prunny.Task_Service.serviceImpl;

import com.prunny.Task_Service.client.ProjectClient;
import com.prunny.Task_Service.dto.CommentDto;
import com.prunny.Task_Service.dto.CommentResponseDto;
import com.prunny.Task_Service.dto.UpdateCommentDto;
import com.prunny.Task_Service.entity.Comment;
import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.exception.MessagingException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.repository.CommentRepository;
import com.prunny.Task_Service.repository.TaskRepository;
import com.prunny.Task_Service.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final TaskRepository taskRepository;
    ModelMapper modelMapper = new ModelMapper();


    public CommentServiceImpl(CommentRepository commentRepository, ProjectClient projectClient, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public CommentResponseDto commentOnTask(CommentDto commentDto)
            throws MessagingException, ResourceNotFoundException {

        Task task = taskRepository.findById(commentDto.getUserTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found "));

        if (commentDto.getText() == null || commentDto.getText().trim().isEmpty()) {
            throw new MessagingException("Comment cannot be empty");
        }

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setUserTaskId(commentDto.getUserTaskId());
        comment.setTask(task);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        // Return the mapped CommentResponseDto
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
        public List<CommentResponseDto> getCommentsByTaskId(Long taskId) throws MessagingException {
            // Validate if the task exists
            if (!taskRepository.existsById(taskId)) {
                throw new MessagingException("Task not found with the specified ID");
            }

            // Fetch comments for the given task
            List<Comment> comments = commentRepository.findByUserTaskId(taskId);
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
    public CommentResponseDto updateComment(Long id, UpdateCommentDto updateCommentDto) throws MessagingException {

        Optional<Comment> commentOptional = commentRepository.findById(id);

        if (commentOptional.isEmpty()) {
            throw new MessagingException("Comment not found");
        }

        Comment comment = commentOptional.get();


        if (updateCommentDto.getText().isEmpty()) {
            throw new MessagingException("Comment text cannot be empty");
        }

        comment.setText(updateCommentDto.getText());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);

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

        commentRepository.deleteById(comment.getCommentId());
    }


}
