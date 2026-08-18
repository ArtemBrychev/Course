package notification.controller;

import lombok.RequiredArgsConstructor;
import notification.dto.NotificationDto;
import notification.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationDto> getNotifications(
            @RequestParam Long userId
    ) {

        return notificationService.getAll(userId);
    }

    @GetMapping("/unread")
    public List<NotificationDto> getUnreadNotifications(
            @RequestParam Long userId
    ) {

        return notificationService.getUnread(userId);
    }

    @PatchMapping("/{id}/read")
    public NotificationDto markAsRead(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {

        return notificationService.markAsRead(
                id,
                userId
        );
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {

        notificationService.delete(
                id,
                userId
        );
    }
}