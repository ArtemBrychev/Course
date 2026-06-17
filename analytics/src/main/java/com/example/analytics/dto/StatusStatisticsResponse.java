package com.example.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusStatisticsResponse {

    private long todo;

    private long inProgress;

    private long done;

}
