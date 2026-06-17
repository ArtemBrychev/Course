package com.example.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistoryResponse {

    private String oldStatus;

    private String newStatus;

    private LocalDateTime occurredAt;
}
