package com.example.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsResponse {

    private long tasksCreated;

    private long commentsCreated;

    private long statusChanges;

    private long tasksCompleted;

}
