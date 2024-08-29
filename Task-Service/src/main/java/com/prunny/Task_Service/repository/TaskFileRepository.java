package com.prunny.Task_Service.repository;

import com.prunny.Task_Service.entity.TaskFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskFileRepository extends JpaRepository<TaskFile, String> {


}
