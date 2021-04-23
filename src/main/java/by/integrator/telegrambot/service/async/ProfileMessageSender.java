package by.integrator.telegrambot.service.async;

import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.client.handler.ClientUpdateHandler;
import by.integrator.telegrambot.bot.message.MessageService;
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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ProfileMessageSender {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProfileMessageSender.class);

    @Autowired
    private ClientService clientService;
    @Autowired
    private MessageSender messageSender;

    private Boolean isWorking = false;

    @Async
    public void runAsync() {
        if (!isWorking) {
            isWorking = true;
            idleMessagesAsync();
        }
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void idleMessagesAsync() {
        LOGGER.info("Profile message service started");
        while (true) {
            try {
                log.info("Slipping: " + calculateDifference());
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

        if (currentHour <= 17 && currentMinutes < 25) {
            Calendar nextCalendar = Calendar.getInstance();

            nextCalendar.set(Calendar.HOUR_OF_DAY, 17);
            nextCalendar.set(Calendar.MINUTE, 25);
            nextCalendar.set(Calendar.SECOND, 0);

            return nextCalendar.getTimeInMillis() - calendar.getTimeInMillis();
        }


        Calendar nextCalendar = Calendar.getInstance();

        nextCalendar.add(Calendar.DATE, 1);
        nextCalendar.set(Calendar.HOUR_OF_DAY, 17);
        nextCalendar.set(Calendar.MINUTE, 25);
        nextCalendar.set(Calendar.SECOND, 0);

        return nextCalendar.getTimeInMillis() - calendar.getTimeInMillis();
    }

    @Async
    protected void sendNotification(Client client) {
        try {
            messageSender.sendMessage(client.getTelegramId(),
                    "Заполните данные о себе что бы мы смоги вам помоч", null);
        }
        catch (TelegramApiException ex) {
            LOGGER.error("Unable to send homework notification to user: " + client.getTelegramId());
        }
    }

    @Async
    protected void check() {
        List<Client> clients = clientService.getByProfileFilledFalse();
        List<Client> clients2 = clientService.getByProfileFilledTrue();
        System.out.println("false " + clients.size());
        System.out.println("true " + clients2.size());

        if (!clients.isEmpty()) {

            for (Client client : clients) {
                sendNotification(client);
            }
        }
    }

}