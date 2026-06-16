package com.example.tracker.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignedPayload implements EventPayload{

    private Long taskId;

    private Long userId;

    private Long assignedBy;

}
