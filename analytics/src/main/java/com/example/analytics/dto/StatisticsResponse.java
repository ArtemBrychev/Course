package com.example.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsResponse {

    private long tasksCreated;

    private long tasksDeleted;

    private long tasksCompleted;

    private long tasksInProgress;

    private long commentsCreated;

    private long boardsCreated;

}
