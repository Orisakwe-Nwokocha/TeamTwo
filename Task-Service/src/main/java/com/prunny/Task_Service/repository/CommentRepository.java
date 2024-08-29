package com.prunny.Task_Service.repository;

import com.prunny.Task_Service.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long id);

    List<Comment> findByProjectId(Long projectId);

    List<Comment> findByTask_TaskId(Long taskId);


}
