package by.integrator.telegrambot.repositories;

import by.integrator.telegrambot.model.Notification;
import by.integrator.telegrambot.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Integer countAllByType(NotificationType notificationType);
    List<Notification> findAllByType(NotificationType notificationType);
}
