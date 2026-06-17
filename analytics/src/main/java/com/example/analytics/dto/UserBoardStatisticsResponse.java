package com.example.analytics.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBoardStatisticsResponse {

    private Long userId;

    private Long boardId;

    private long tasksCreated;

    private long commentsCreated;

    private long statusChanges;

}
