package com.example.analytics.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTaskStatisticsResponse {

    private Long userId;

    private Long taskId;

    private List<UserTaskActionResponse> actions;

}
