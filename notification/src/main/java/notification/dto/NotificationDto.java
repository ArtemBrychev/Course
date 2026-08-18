package notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import notification.model.NotificationType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private Long id;

    private Long userId;

    private Long boardId;

    private Long taskId;

    private NotificationType type;

    private String text;

    private LocalDateTime createdAt;

    private boolean read;
}