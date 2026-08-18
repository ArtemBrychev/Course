package notification.repository;

import notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    List<Notification> findAllByUserIdAndReadFalseOrderByCreatedAtDesc(
            Long userId
    );

    Optional<Notification> findByIdAndUserId(
            Long id,
            Long userId
    );
}