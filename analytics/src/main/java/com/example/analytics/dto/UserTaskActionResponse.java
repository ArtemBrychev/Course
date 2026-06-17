package com.example.analytics.dto;

import com.example.analytics.eventdriven.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTaskActionResponse {

    private EventType eventType;

    private LocalDateTime occurredAt;

}
