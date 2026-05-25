package com.example.tracker.dto;

import lombok.Data;

@Data
public class CreateTaskRequest {

    private Long boardId;

    private String title;

    private String description;
}