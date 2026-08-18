package notification.eventdriven;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notification.eventdriven.events.BoardMemberAddedPayload;
import notification.eventdriven.events.BoardMemberRemovedPayload;
import notification.eventdriven.events.CommentCreatedPayload;
import notification.eventdriven.events.TaskAssignedPayload;
import notification.eventdriven.events.TaskStatusChangedPayload;
import notification.model.BoardMember;
import notification.model.Notification;
import notification.model.NotificationType;
import notification.repository.BoardMemberRepository;
import notification.repository.NotificationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandler {

    private final ObjectMapper objectMapper;

    private final BoardMemberRepository boardMemberRepository;
    private final NotificationRepository notificationRepository;

    public void handle(EventMessage event) throws JsonProcessingException {

        switch (event.getEventType()) {

            case TASK_ASSIGNED -> {
                TaskAssignedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                TaskAssignedPayload.class
                        );

                handleTaskAssigned(payload, event);
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

            case BOARD_MEMBER_ADDED -> {
                BoardMemberAddedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                BoardMemberAddedPayload.class
                        );

                handleBoardMemberAdded(payload, event);
            }

            case BOARD_MEMBER_REMOVED -> {
                BoardMemberRemovedPayload payload =
                        objectMapper.treeToValue(
                                event.getPayload(),
                                BoardMemberRemovedPayload.class
                        );

                handleBoardMemberRemoved(payload);
            }

            default -> log.info(
                    "Event with type {} is ignored",
                    event.getEventType()
            );
        }
    }

    private void handleTaskAssigned(
            TaskAssignedPayload payload,
            EventMessage event
    ) {

        notificationRepository.save(
                new Notification(
                        null,
                        payload.getAssigneeId(),
                        payload.getBoardId(),
                        payload.getTaskId(),
                        NotificationType.TASK_ASSIGNED,
                        "Вам назначена задача",
                        event.getOccurredAt(),
                        false
                )
        );
    }

    private void handleTaskStatusChanged(
            TaskStatusChangedPayload payload,
            EventMessage event
    ) {

        boardMemberRepository
                .findAllByBoardId(payload.getBoardId())
                .stream()
                .filter(member ->
                        !member.getUserId().equals(
                                payload.getChangedBy()
                        )
                )
                .forEach(member ->
                        notificationRepository.save(
                                new Notification(
                                        null,
                                        member.getUserId(),
                                        payload.getBoardId(),
                                        payload.getTaskId(),
                                        NotificationType.TASK_STATUS_CHANGED,
                                        "Статус задачи изменён",
                                        event.getOccurredAt(),
                                        false
                                )
                        )
                );
    }

    private void handleCommentCreated(
            CommentCreatedPayload payload,
            EventMessage event
    ) {

        boardMemberRepository
                .findAllByBoardId(payload.getBoardId())
                .stream()
                .filter(member ->
                        !member.getUserId().equals(
                                payload.getAuthorId()
                        )
                )
                .forEach(member ->
                        notificationRepository.save(
                                new Notification(
                                        null,
                                        member.getUserId(),
                                        payload.getBoardId(),
                                        payload.getTaskId(),
                                        NotificationType.COMMENT_CREATED,
                                        "Добавлен новый комментарий",
                                        event.getOccurredAt(),
                                        false
                                )
                        )
                );
    }

    private void handleBoardMemberAdded(
            BoardMemberAddedPayload payload,
            EventMessage event
    ) {

        boardMemberRepository.save(
                new BoardMember(
                        null,
                        payload.getBoardId(),
                        payload.getMemberId()
                )
        );

        notificationRepository.save(
                new Notification(
                        null,
                        payload.getMemberId(),
                        payload.getBoardId(),
                        null,
                        NotificationType.BOARD_MEMBER_ADDED,
                        "Вас добавили на доску",
                        event.getOccurredAt(),
                        false
                )
        );
    }

    private void handleBoardMemberRemoved(
            BoardMemberRemovedPayload payload
    ) {

        boardMemberRepository.deleteByBoardIdAndUserId(
                payload.getBoardId(),
                payload.getMemberId()
        );
    }
}