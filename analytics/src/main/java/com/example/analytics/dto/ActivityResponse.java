package com.example.analytics.dto;

import com.example.analytics.eventdriven.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponse {

    private EventType eventType;

    private Long userId;

    private Long taskId;

    private LocalDateTime occurredAt;

    private String payload;
}
