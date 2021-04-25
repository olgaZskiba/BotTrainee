package by.integrator.telegrambot.service.async;

import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.PostponeMessage;
import by.integrator.telegrambot.model.User;
import by.integrator.telegrambot.service.ClientService;
import by.integrator.telegrambot.service.PostponeMessageService;
import by.integrator.telegrambot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class PostponeMessageSender {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSender messageService;
    @Autowired
    private ClientService clientService;

    @Autowired
    private PostponeMessageService postponeMessageService;

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
        log.info("Postpone message service started");
        while (true) {
            List<PostponeMessage> postponeMessages = postponeMessageService.getAllWithoutLast();

            LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

            for (PostponeMessage postponeMessage : postponeMessages) {
                LocalDateTime messageDateTime = postponeMessage.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                if (localDateTime.isEqual(messageDateTime)) {
                    sendMessageToClients(postponeMessage);
                    postponeMessageService.delete(postponeMessage);
                }
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                log.info("Postpone message service stopped: " + ex.getMessage());
            }
        }
    }

    @Async
    protected void sendMessageToClients(PostponeMessage postponeMessage) {
        List<Client> clients = clientService.getAll();
        for (Client client : clients) {
            if (postponeMessage.getPictureUrl() != null) {
                sendPostponeMessageWithPicture(postponeMessage, client.getUser());
            } else {
                sendPostponeMessage(postponeMessage, client.getUser());
            }
        }
    }

    @Async
    protected void sendPostponeMessageWithPicture(PostponeMessage postponeMessage, User user) {
        SendPhoto sendPhoto = new SendPhoto();

        InputFile file = new InputFile();
        file.setMedia(postponeMessage.getPictureUrl());

        sendPhoto.setPhoto(file);
        sendPhoto.setCaption(postponeMessage.getText());
        sendPhoto.setChatId(user.getTelegramId());

        try {
            messageService.sendPhoto(sendPhoto);
        }
        catch (TelegramApiException ex) {
            log.error("Error sending postponed message with picture: " + ex.getLocalizedMessage());
        }
    }

    @Async
    protected void sendPostponeMessage(PostponeMessage postponeMessage, User user) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.setText(postponeMessage.getText());
        sendMessage.setChatId(user.getTelegramId());
        sendMessage.setParseMode("HTML");

        try {
            messageService.sendMessage(sendMessage);
        }
        catch (TelegramApiException ex) {
            log.error("Error sending postponed message: " + ex.getLocalizedMessage());
        }
    }
}