package by.integrator.telegrambot.service;

import by.integrator.telegrambot.model.Notification;
import by.integrator.telegrambot.model.enums.NotificationType;
import by.integrator.telegrambot.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {
    @Autowired private NotificationRepository notificationRepository;

    @Transactional
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    @Transactional
    public void delete(Notification notification) {
        notificationRepository.delete(notification);
    }

    @Transactional
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    @Transactional
    public List<Notification> getAllByType(NotificationType type) {
        return notificationRepository.findAllByType(type);
    }

    @Transactional
    public Integer getCountByType(NotificationType type) {
        return notificationRepository.countAllByType(type);
    }

    public void createNotification(String text, NotificationType type) {
        Notification notification = Notification.builder()
                .type(type)
                .text(text)
                .build();
        save(notification);
    }

    @Transactional
    public Notification getById(int id) {
        return notificationRepository.findById((long)id).orElse(null);
    }
}
