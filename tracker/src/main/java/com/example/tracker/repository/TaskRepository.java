package com.example.tracker.repository;

import com.example.tracker.model.Task;
import com.example.tracker.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByBoardId(Long boardId);

    List<Task> findAllByAssigneeId(Long assigneeId);

    List<Task> findAllByStatus(TaskStatus status);

    long countByStatus(TaskStatus status);
}