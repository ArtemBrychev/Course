package com.example.analytics.eventdriven;

import com.example.analytics.eventdriven.events.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandler {

    public void handleBoardCreated(
            BoardCreatedPayload payload,
            LocalDateTime occurredAt
    ) {
        log.info("Is this shit working? " + payload.toString());
    }

    public void handleBoardDeleted(
            BoardDeletedPayload payload,
            LocalDateTime occurredAt
    ) {
        log.info("Board deleted: " + payload.toString());
    }

    public void handleTaskCreated(
            TaskCreatedPayload payload,
            LocalDateTime occurredAt
    ) {
        log.info("Task created: " + payload.toString());
    }

    public void handleTaskUpdated(
            TaskUpdatedPayload payload,
            LocalDateTime occurredAt
    ) {
        log.info("Task updated: " + payload.toString());
    }

    public void handleTaskDeleted(
            TaskDeletedPayload payload,
            LocalDateTime occurredAt
    ) {
        log.info("Task deleted: " + payload.toString());
    }

    public void handleTaskStatusChanged(
            TaskStatusChangedPayload payload,
            LocalDateTime occurredAt
    ) {
        log.info("Task status changed: " + payload.toString());
    }

    public void handleCommentCreated(
            CommentCreatedPayload payload,
            LocalDateTime occurredAt
    ) {
        log.info("Comment created: " + payload.toString());
    }

    public void handleCommentDeleted(
            CommentDeletedPayload payload,
            LocalDateTime occurredAt
    ) {
        log.info("Comment deleted: " + payload.toString());
    }

}