package com.prunny.Task_Service.service;

import com.prunny.Task_Service.dto.CommentDto;
import com.prunny.Task_Service.dto.CommentResponseDto;
import com.prunny.Task_Service.exception.MessagingException;
import com.prunny.Task_Service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {


    CommentResponseDto commentOnTask( CommentDto commentDto) throws MessagingException, ResourceNotFoundException;

    CommentResponseDto getCommentById(long id) throws MessagingException;

   // List<CommentResponseDto> getCommentsByProject(Long projectId)throws MessagingException ;

    List<CommentResponseDto> getCommentsByTaskId( Long taskId)  throws MessagingException;

    //,String authentication
    CommentResponseDto updateComment(Long id, String text) throws MessagingException;

    //,String authorization
    void deleteComment(Long id)throws MessagingException;


}
