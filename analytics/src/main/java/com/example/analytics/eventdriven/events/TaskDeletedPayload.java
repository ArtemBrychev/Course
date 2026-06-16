package com.example.analytics.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDeletedPayload implements EventPayload{

    private Long taskId;

    private Long deletedBy;

}
