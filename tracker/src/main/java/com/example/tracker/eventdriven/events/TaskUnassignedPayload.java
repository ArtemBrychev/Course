package com.example.tracker.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUnassignedPayload implements EventPayload{
    private Long taskId;

    private Long boardId;

    private Long assigneeId;

    private Long createdBy;
}
