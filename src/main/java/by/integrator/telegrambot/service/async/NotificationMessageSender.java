package by.integrator.telegrambot.service.async;

import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.client.service.ClientMessageSource;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class NotificationMessageSender {
    private final static Logger LOGGER = LoggerFactory.getLogger(NotificationMessageSender.class);

    @Autowired
    private ClientService clientService;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private ClientMessageSource clientMessageSource;

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
        LOGGER.info("Notification message service started");
        while (true) {
            try {
                log.info("Notification Slipping: " + calculateDifference());
                Thread.sleep(calculateDifference());
                check();
            } catch (InterruptedException ex) {
                log.info("Profile message service stopped: " + ex.getMessage());
            }
        }
    }

    private Long calculateDifference() {
        Calendar calendar = Calendar.getInstance();

        Integer currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer currentMinutes = calendar.get(Calendar.MINUTE);

        if (currentHour <= 15 && currentMinutes < 57) {
            Calendar nextCalendar = Calendar.getInstance();

            nextCalendar.set(Calendar.HOUR_OF_DAY, 15);
            nextCalendar.set(Calendar.MINUTE, 57);
            nextCalendar.set(Calendar.SECOND, 0);

            return nextCalendar.getTimeInMillis() - calendar.getTimeInMillis();
        }

        Calendar nextCalendar = Calendar.getInstance();

        nextCalendar.add(Calendar.DATE, 1);
        nextCalendar.set(Calendar.HOUR_OF_DAY, 15);
        nextCalendar.set(Calendar.MINUTE, 57);
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
            LOGGER.error("Unable to sendNotification to user: " + client.getTelegramId());
        }
    }

    @Async
    protected void check() {
        List<Client> clients = clientService.getByProfileFilledTrue();

        if (!clients.isEmpty()) {
            for (Client client : clients) {
                String notification = getNotificationByClient(client);
                if (!notification.equals("")) {
                    sendNotification(client, notification);
                    int nextDay = client.getDay();
                    client.setDay(++nextDay);
                    clientService.save(client);
                }
            }
        }
    }

    private String getNotificationByClient(Client client) {
        if (client.getDay() == 1) {
            return clientMessageSource.getMessage("message.postAnonsOne");
        } else if (client.getDay() == 2) {
            return clientMessageSource.getMessage("message.postAnonsTwo");
        } else if (client.getDay() == 3) {
            return clientMessageSource.getMessage("message.postAnonsThree");
        } else if (client.getDay() == 4) {
            return clientMessageSource.getMessage("message.postAnonsFour");
        }
        return "";
    }
}
