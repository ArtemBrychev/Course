package com.example.tracker.dto;

import com.example.tracker.model.TaskStatus;
import lombok.Data;

@Data
public class UpdateTaskRequest {

    private String title;

    private String description;

    private TaskStatus status;

    private Long assigneeId;
}