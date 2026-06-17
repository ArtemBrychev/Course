package com.example.analytics.model;

import com.example.analytics.eventdriven.EventType;
import com.example.analytics.eventdriven.events.TaskStatusChangedPayload;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "status_transitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusTransitionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long taskId;

    private Long boardId;

    private Long userId;

    private String oldStatus;

    private String newStatus;

    private LocalDateTime occurredAt;

    public StatusTransitionEntity(
            TaskStatusChangedPayload payload,
            LocalDateTime occurredAt
    ) {
        this.taskId = payload.getTaskId();
        this.boardId = payload.getBoardId();
        this.userId = payload.getChangedBy();
        this.oldStatus = payload.getOldStatus();
        this.newStatus = payload.getNewStatus();
        this.occurredAt = occurredAt;
    }

}
