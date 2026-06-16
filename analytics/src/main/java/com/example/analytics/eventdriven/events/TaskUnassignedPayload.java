package com.example.analytics.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUnassignedPayload implements EventPayload{

    private Long taskId;

    private Long userId;

    private Long unassignedBy;

}
