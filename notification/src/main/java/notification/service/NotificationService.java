package notification.service;

import lombok.RequiredArgsConstructor;
import notification.dto.NotificationDto;
import notification.exceptions.DataNotFoundException;
import notification.model.Notification;
import notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationDto> getAll(Long userId) {

        return notificationRepository
                .findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<NotificationDto> getUnread(Long userId) {

        return notificationRepository
                .findAllByUserIdAndReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public NotificationDto markAsRead(
            Long notificationId,
            Long userId
    ) {

        Notification notification =
                notificationRepository
                        .findByIdAndUserId(
                                notificationId,
                                userId
                        )
                        .orElseThrow(() ->
                                new DataNotFoundException(
                                        "Уведомление не найдено"
                                )
                        );

        notification.setRead(true);

        return toDto(
                notificationRepository.save(notification)
        );
    }

    public void delete(
            Long notificationId,
            Long userId
    ) {

        Notification notification =
                notificationRepository
                        .findByIdAndUserId(
                                notificationId,
                                userId
                        )
                        .orElseThrow(() ->
                                new DataNotFoundException(
                                        "Уведомление не найдено"
                                )
                        );

        notificationRepository.delete(notification);
    }

    private NotificationDto toDto(
            Notification notification
    ) {

        return new NotificationDto(
                notification.getId(),
                notification.getUserId(),
                notification.getBoardId(),
                notification.getTaskId(),
                notification.getType(),
                notification.getText(),
                notification.getCreatedAt(),
                notification.isRead()
        );
    }
}