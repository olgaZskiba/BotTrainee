package by.integrator.telegrambot.service.async;

import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.client.keyboard.inline.ClientInlineKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.client.service.ClientMessageSource;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.service.ClientService;
import by.integrator.telegrambot.service.MessengerService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class ProfileMessageSender {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProfileMessageSender.class);

    @Autowired
    private ClientService clientService;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private ClientMessageSource clientMessageSource;
    @Autowired
    private MessengerService messengerService;
    @Autowired
    private ClientInlineKeyboardMarkupSource clientInlineKeyboardMarkupSource;

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

        if (currentHour <= 11 && currentMinutes < 30) {
            Calendar nextCalendar = Calendar.getInstance();

            nextCalendar.set(Calendar.HOUR_OF_DAY, 11);
            nextCalendar.set(Calendar.MINUTE, 30);
            nextCalendar.set(Calendar.SECOND, 0);

            return nextCalendar.getTimeInMillis() - calendar.getTimeInMillis();
        }


        Calendar nextCalendar = Calendar.getInstance();

        nextCalendar.add(Calendar.DATE, 1);
        nextCalendar.set(Calendar.HOUR_OF_DAY, 11);
        nextCalendar.set(Calendar.MINUTE, 30);
        nextCalendar.set(Calendar.SECOND, 0);

        return nextCalendar.getTimeInMillis() - calendar.getTimeInMillis();
    }

    @Async
    protected void sendNotification(Client client, String message, ReplyKeyboard replyKeyboard) {
        try {
            messageSender.sendMessage(client.getTelegramId(),
                    message, replyKeyboard);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to send Notification notification to user: " + client.getTelegramId());
        }
    }

    @Async
    protected void check() {
        List<Client> clients = clientService.getByFillStartedTrue();

        if (!clients.isEmpty()) {

            for (Client client : clients) {

                if (client.getProblem() == null ||
                        client.getGoals() == null ||
                        client.getFieldOfActivity() == null ||
                        messengerService.getAllByClient(client).isEmpty() ||
                        client.getIntegrationToCrm() == null) {
                    if (!getNotificationBefore(client).equals("")) {
                        sendNotification(client, getNotificationBefore(client), null);
                    }
                } else if (client.getPhoneNumber() == null &&
                        client.getWayCommunication() == null &&
                        !client.getProcessed() && !getNotificationAfter(client).equals("")) {
                    sendNotification(client, getNotificationAfter(client),
                            clientInlineKeyboardMarkupSource.getFreeConsultationKeyboard());
                }
            }
        }
    }

    public String getNotificationBefore(Client client) {
        if (client.getDay() == 1) {
            return clientMessageSource.getMessage("message.one", client.getFirstName());
        } else if (client.getDay() == 2) {
            return clientMessageSource.getMessage("message.two");
        } else if (client.getDay() == 3) {
            return clientMessageSource.getMessage("message.three", client.getFirstName());
        } else if (client.getDay() == 4) {
            return clientMessageSource.getMessage("message.four");
        } else if (client.getDay() == 5) {
            return clientMessageSource.getMessage("message.five", client.getFirstName());
        }
        return "";
    }

    public String getNotificationAfter(Client client) {
        int number = 1 + (int) ( Math.random() * 5);
        if (number == 1) {
            return clientMessageSource.getMessage("message.six", client.getFirstName());
        } else if (number == 2) {
            return clientMessageSource.getMessage("message.seven");
        } else if (number == 3) {
            return clientMessageSource.getMessage("message.eight", client.getFirstName());
        } else if (number == 4) {
            return clientMessageSource.getMessage("message.nine", client.getFirstName());
        } else if (number == 5) {
            return clientMessageSource.getMessage("message.ten");
        }
        return "";
    }

}