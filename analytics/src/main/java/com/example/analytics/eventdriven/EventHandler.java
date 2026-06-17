package com.example.analytics.eventdriven;

import com.example.analytics.eventdriven.events.*;
import com.example.analytics.model.*;
import com.example.analytics.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandler {

    private final ObjectMapper objectMapper;

    private final EventRepository eventRepository;
    private final TaskStateRepository taskStateRepository;
    private final StatusTransitionRepository statusTransitionRepository;

    public void handle(EventMessage event) throws JsonProcessingException {

        switch (event.getEventType()) {

            case BOARD_CREATED -> {
                BoardCreatedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                BoardCreatedPayload.class
                        );

                handleBoardCreated(payload, event);
            }

            case BOARD_DELETED -> {
                BoardDeletedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                BoardDeletedPayload.class
                        );

                handleBoardDeleted(payload, event);
            }

            case TASK_CREATED -> {
                TaskCreatedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                TaskCreatedPayload.class
                        );

                handleTaskCreated(payload, event);
            }

            case TASK_UPDATED -> {
                TaskUpdatedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                TaskUpdatedPayload.class
                        );

                handleTaskUpdated(payload, event);
            }

            case TASK_DELETED -> {
                TaskDeletedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                TaskDeletedPayload.class
                        );

                handleTaskDeleted(payload, event);
            }

            case TASK_STATUS_CHANGED -> {
                TaskStatusChangedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                TaskStatusChangedPayload.class
                        );

                handleTaskStatusChanged(payload, event);
            }

            case COMMENT_CREATED -> {
                CommentCreatedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                CommentCreatedPayload.class
                        );

                handleCommentCreated(payload, event);
            }

            case COMMENT_DELETED -> {
                CommentDeletedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                CommentDeletedPayload.class
                        );

                handleCommentDeleted(payload, event);
            }
        }
    }

    private void handleBoardCreated(
            BoardCreatedPayload payload,
            EventMessage event
    ) {
        eventRepository.save(
                new EventEntity(
                        null,
                        EventType.BOARD_CREATED,
                        payload.getOwnerId(),
                        payload.getBoardId(),
                        null,
                        event.getPayload().toString(),
                        event.getOccurredAt()
                )
        );
    }

    private void handleBoardDeleted(
            BoardDeletedPayload payload,
            EventMessage event
    ) {
        eventRepository.save(
                new EventEntity(
                        null,
                        EventType.BOARD_DELETED,
                        payload.getDeletedBy(),
                        payload.getBoardId(),
                        null,
                        event.getPayload().toString(),
                        event.getOccurredAt()
                )
        );
    }

    private void handleTaskCreated(
            TaskCreatedPayload payload,
            EventMessage event
    ) {

        eventRepository.save(
                new EventEntity(
                        null,
                        EventType.TASK_CREATED,
                        payload.getCreatedBy(),
                        payload.getBoardId(),
                        payload.getTaskId(),
                        event.getPayload().toString(),
                        event.getOccurredAt()
                )
        );

        taskStateRepository.save(
                new TaskStateEntity(
                        payload,
                        event.getOccurredAt()
                )
        );
    }


    private void handleTaskUpdated(
            TaskUpdatedPayload payload,
            EventMessage event
    ) {

        eventRepository.save(
                new EventEntity(
                        null,
                        EventType.TASK_UPDATED,
                        payload.getUpdatedBy(),
                        payload.getBoardId(),
                        payload.getTaskId(),
                        event.getPayload().toString(),
                        event.getOccurredAt()
                )
        );

        TaskStateEntity task =
                taskStateRepository.findById(
                        payload.getTaskId()
                ).orElseThrow();

        task.setUpdatedAt(
                event.getOccurredAt()
        );

        taskStateRepository.save(task);
    }

    private void handleTaskDeleted(
            TaskDeletedPayload payload,
            EventMessage event
    ) {

        eventRepository.save(
                new EventEntity(
                        null,
                        EventType.TASK_DELETED,
                        payload.getDeletedBy(),
                        payload.getBoardId(),
                        payload.getTaskId(),
                        event.getPayload().toString(),
                        event.getOccurredAt()
                )
        );

        taskStateRepository.deleteById(
                payload.getTaskId()
        );
    }

    private void handleTaskStatusChanged(
            TaskStatusChangedPayload payload,
            EventMessage event
    ) {

        eventRepository.save(
                new EventEntity(
                        null,
                        EventType.TASK_STATUS_CHANGED,
                        payload.getChangedBy(),
                        payload.getBoardId(),
                        payload.getTaskId(),
                        event.getPayload().toString(),
                        event.getOccurredAt()
                )
        );

        statusTransitionRepository.save(
                new StatusTransitionEntity(
                        payload,
                        event.getOccurredAt()
                )
        );

        TaskStateEntity task =
                taskStateRepository.findById(
                        payload.getTaskId()
                ).orElseThrow();

        task.setStatus(
                payload.getNewStatus()
        );

        task.setUpdatedAt(
                event.getOccurredAt()
        );

        if ("DONE".equals(payload.getNewStatus())) {
            task.setCompletedAt(
                    event.getOccurredAt()
            );
        }

        taskStateRepository.save(task);
    }

    private void handleCommentCreated(
            CommentCreatedPayload payload,
            EventMessage event
    ) {

        eventRepository.save(
                new EventEntity(
                        null,
                        EventType.COMMENT_CREATED,
                        payload.getAuthorId(),
                        payload.getBoardId(),
                        payload.getTaskId(),
                        event.getPayload().toString(),
                        event.getOccurredAt()
                )
        );
    }

    private void handleCommentDeleted(
            CommentDeletedPayload payload,
            EventMessage event
    ) {

        eventRepository.save(
                new EventEntity(
                        null,
                        EventType.COMMENT_DELETED,
                        payload.getDeletedBy(),
                        payload.getBoardId(),
                        payload.getTaskId(),
                        event.getPayload().toString(),
                        event.getOccurredAt()
                )
        );
    }


}