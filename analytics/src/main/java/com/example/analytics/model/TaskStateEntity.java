package com.example.analytics.model;

import com.example.analytics.eventdriven.EventType;
import jakarta.persistence.*;
import com.example.analytics.eventdriven.events.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_state")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskStateEntity {

    @Id
    private Long taskId;

    private Long boardId;

    private Long assigneeId;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    public TaskStateEntity(
            TaskCreatedPayload payload,
            LocalDateTime occurredAt
    ) {
        this.taskId = payload.getTaskId();
        this.boardId = payload.getBoardId();
        this.assigneeId = payload.getAssigneeId();
        this.status = payload.getStatus();
        this.createdAt = occurredAt;
        this.updatedAt = occurredAt;
        this.completedAt = null;
    }

    public TaskStateEntity(
            TaskUpdatedPayload payload,
            LocalDateTime occurredAt
    ) {
        this.taskId = payload.getTaskId();
        this.boardId = payload.getBoardId();
        this.assigneeId = null;
        this.status = null;
        this.createdAt = null;
        this.updatedAt = occurredAt;
        this.completedAt = null;
    }

    public TaskStateEntity(
            TaskDeletedPayload payload,
            LocalDateTime occurredAt
    ) {
        this.taskId = payload.getTaskId();
        this.boardId = payload.getBoardId();
        this.assigneeId = null;
        this.status = null;
        this.createdAt = null;
        this.updatedAt = occurredAt;
        this.completedAt = null;
    }

    public TaskStateEntity(
            TaskStatusChangedPayload payload,
            LocalDateTime occurredAt
    ) {
        this.taskId = payload.getTaskId();
        this.boardId = payload.getBoardId();
        this.assigneeId = null;
        this.status = payload.getNewStatus();
        this.createdAt = null;
        this.updatedAt = occurredAt;

        if ("DONE".equals(payload.getNewStatus())) {
            this.completedAt = occurredAt;
        } else {
            this.completedAt = null;
        }
    }

}
