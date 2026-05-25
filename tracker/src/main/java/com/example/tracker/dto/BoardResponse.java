package com.example.tracker.dto;

import com.example.tracker.model.Board;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardResponse {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private Long ownerId;

    public static BoardResponse from(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .createdAt(board.getCreatedAt())
                .ownerId(board.getOwner().getId())
                .build();
    }
}