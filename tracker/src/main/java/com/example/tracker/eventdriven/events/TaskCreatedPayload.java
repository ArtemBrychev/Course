package com.example.tracker.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreatedPayload implements EventPayload{

    private Long taskId;

    private Long boardId;

    private String title;

    private String description;

    private String status;

    private Long assigneeId;

    private Long createdBy;

}
