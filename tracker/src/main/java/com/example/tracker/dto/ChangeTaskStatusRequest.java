package com.example.tracker.dto;

import com.example.tracker.model.TaskStatus;
import lombok.Data;

@Data
public class ChangeTaskStatusRequest {

    private TaskStatus status;
}