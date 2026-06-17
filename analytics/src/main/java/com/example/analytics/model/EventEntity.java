package com.example.analytics.model;

import com.example.analytics.eventdriven.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "events",
        indexes = {
                @Index(name = "idx_event_task", columnList = "taskId"),
                @Index(name = "idx_event_user", columnList = "userId"),
                @Index(name = "idx_event_board", columnList = "boardId"),
                @Index(name = "idx_event_time", columnList = "occurredAt")
        }
)
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private Long userId;

    private Long boardId;

    private Long taskId;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private LocalDateTime occurredAt;
}
