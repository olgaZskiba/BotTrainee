package by.integrator.telegrambot.service.async;

import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.client.service.ClientMessageSource;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.Notification;
import by.integrator.telegrambot.model.enums.NotificationType;
import by.integrator.telegrambot.service.ClientService;
import by.integrator.telegrambot.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class NotificationBeforeMessageSender {
    private final static Logger LOGGER = LoggerFactory.getLogger(NotificationBeforeMessageSender.class);

    @Autowired
    private ClientService clientService;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private ClientMessageSource clientMessageSource;
    @Autowired
    private NotificationService notificationService;

    private Boolean isWorking = false;

    @Async
    public void runAsync() {
        if (!isWorking) {
            isWorking = true;
            idleMessagesAsync();
        }
    }

    @Async
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void idleMessagesAsync() {
        LOGGER.info("Notification before consultation message service started");
        while (true) {
            try {
                log.info("Notification before consultation Slipping: " + calculateDifference());
                Thread.sleep(calculateDifference());
                check();
            } catch (InterruptedException ex) {
                log.info("Notification before consultation message service stopped: " + ex.getMessage());
            }
        }
    }

    private Long calculateDifference() {
        Calendar calendar = Calendar.getInstance();

        Integer currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer currentMinutes = calendar.get(Calendar.MINUTE);

        if (currentHour <= 12 && currentMinutes < 30) {
            Calendar nextCalendar = Calendar.getInstance();

            nextCalendar.set(Calendar.HOUR_OF_DAY, 12);
            nextCalendar.set(Calendar.MINUTE, 30);
            nextCalendar.set(Calendar.SECOND, 0);

            return nextCalendar.getTimeInMillis() - calendar.getTimeInMillis();
        }

        Calendar nextCalendar = Calendar.getInstance();

        nextCalendar.add(Calendar.DATE, 1);
        nextCalendar.set(Calendar.HOUR_OF_DAY, 12);
        nextCalendar.set(Calendar.MINUTE, 30);
        nextCalendar.set(Calendar.SECOND, 0);

        return nextCalendar.getTimeInMillis() - calendar.getTimeInMillis();
    }

    @Async
    protected void sendNotification(Client client, String message) {
        try {
            messageSender.sendMessage(client.getTelegramId(),
                    message, null);
        }
        catch (TelegramApiException ex) {
            LOGGER.error("Unable to send notification before consultation to user: " + client.getTelegramId());
        }
    }

    @Async
    protected void check() {
        List<Client> clients = clientService.getByProcessedFalse();

        if (!clients.isEmpty()) {
            for (Client client : clients) {
                String notification = getNotificationByClient(client);
                if (!notification.equals("")) {
                    sendNotification(client, notification);
                }
            }
        }
    }

    private String getNotificationByClient(Client client) {
        List<Notification> notifications = notificationService.getAllByType(NotificationType.BEFORE);
        if (!notifications.isEmpty()) {
            if (client.getDay() <= notifications.size()) {
                return notifications.get(client.getDay()).getText();
            }
        }
        return "";
    }
}
