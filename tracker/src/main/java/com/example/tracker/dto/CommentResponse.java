package com.example.tracker.dto;

import com.example.tracker.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long id;

    private String text;

    private Long taskId;

    private Long authorId;

    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {

        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .taskId(comment.getTask().getId())
                .authorId(comment.getAuthor().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}