package com.example.tracker.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusChangedPayload implements EventPayload{

    private Long taskId;

    private String oldStatus;

    private String newStatus;

    private Long changedBy;

}
