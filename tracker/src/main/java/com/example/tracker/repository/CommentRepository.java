package com.example.tracker.repository;

import com.example.tracker.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTaskId(Long taskId);

    List<Comment> findAllByAuthorId(Long authorId);
}