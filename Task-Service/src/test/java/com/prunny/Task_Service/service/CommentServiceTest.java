package com.prunny.Task_Service.service;

import com.prunny.Task_Service.dto.CommentDto;
import com.prunny.Task_Service.dto.CommentResponseDto;
import com.prunny.Task_Service.entity.Comment;
import com.prunny.Task_Service.entity.Task;
import com.prunny.Task_Service.exception.MessagingException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import com.prunny.Task_Service.repository.CommentRepository;
import com.prunny.Task_Service.repository.TaskRepository;
import com.prunny.Task_Service.serviceImpl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment comment;
    private Task task;
    private CommentDto commentDto;
    private CommentResponseDto commentResponseDto;

    @BeforeEach
    void setUp() {
        // Initialize test data
        task = new Task();
        task.setTaskId(1L);
        task.setTaskName("Sample Task");

        comment = new Comment();
        comment.setCommentId(1L);
        comment.setText("Sample Comment");
        comment.setTask(task);
        comment.setCreatedAt(LocalDateTime.now());

        commentDto = new CommentDto();
        commentDto.setUserTaskId(1L);
        commentDto.setText("Sample Comment Text");


        commentResponseDto = new ModelMapper().map(comment, CommentResponseDto.class);
    }

    @Test
    void testCommentOnTaskSuccess() throws MessagingException, ResourceNotFoundException {

        when(taskRepository.findById(commentDto.getUserTaskId())).thenReturn(Optional.of(task));

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);


        CommentResponseDto result = commentService.commentOnTask(commentDto);

        assertNotNull(result);
        assertEquals("Sample Comment Text", result.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }


    @Test
    public void testUpdateComment() throws Exception {
        Comment comment = new Comment();
        comment.setUserTaskId(1L);
        comment.setText("Old Comment");

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDto response = commentService.updateComment(1L, "Updated Comment");

        assertNotNull(response);
        assertEquals("Updated Comment", response.getText());
        verify(commentRepository, times(1)).findById(anyLong());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }


    @Test
    public void testDeleteComment() throws Exception {
        Comment comment = new Comment();
        comment.setUserTaskId(1L);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> commentService.deleteComment(1L));

        verify(commentRepository, times(1)).findById(anyLong());
        verify(commentRepository, times(1)).deleteById(anyLong());
    }
}
