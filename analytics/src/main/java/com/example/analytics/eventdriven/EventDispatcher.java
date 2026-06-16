package com.example.analytics.eventdriven;

import com.example.analytics.eventdriven.events.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventDispatcher {

    private final ObjectMapper objectMapper;

    private final EventHandler eventHandler;

    public void dispatch(EventMessage event) throws JsonProcessingException {

        switch (event.getEventType()) {

            case BOARD_CREATED -> {
                BoardCreatedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                BoardCreatedPayload.class
                        );

                eventHandler.handleBoardCreated(
                        payload,
                        event.getOccurredAt()
                );
            }

            case BOARD_DELETED -> {
                BoardDeletedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                BoardDeletedPayload.class
                        );

                eventHandler.handleBoardDeleted(
                        payload,
                        event.getOccurredAt()
                );
            }

            case TASK_CREATED -> {
                TaskCreatedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                TaskCreatedPayload.class
                        );

                eventHandler.handleTaskCreated(
                        payload,
                        event.getOccurredAt()
                );
            }

            case TASK_UPDATED -> {
                TaskUpdatedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                TaskUpdatedPayload.class
                        );

                eventHandler.handleTaskUpdated(
                        payload,
                        event.getOccurredAt()
                );
            }

            case TASK_DELETED -> {
                TaskDeletedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                TaskDeletedPayload.class
                        );

                eventHandler.handleTaskDeleted(
                        payload,
                        event.getOccurredAt()
                );
            }

            case TASK_STATUS_CHANGED -> {
                TaskStatusChangedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                TaskStatusChangedPayload.class
                        );

                eventHandler.handleTaskStatusChanged(
                        payload,
                        event.getOccurredAt()
                );
            }

            case COMMENT_CREATED -> {
                CommentCreatedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                CommentCreatedPayload.class
                        );

                eventHandler.handleCommentCreated(
                        payload,
                        event.getOccurredAt()
                );
            }

            case COMMENT_DELETED -> {
                CommentDeletedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                CommentDeletedPayload.class
                        );

                eventHandler.handleCommentDeleted(
                        payload,
                        event.getOccurredAt()
                );
            }
        }
    }
}