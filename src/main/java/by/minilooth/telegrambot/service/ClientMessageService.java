package by.minilooth.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import by.minilooth.telegrambot.bot.context.client.ClientBotContext;
import by.minilooth.telegrambot.bot.message.MessageService;
import by.minilooth.telegrambot.bot.message.client.ClientMessageSource;
import by.minilooth.telegrambot.bot.service.MessageSender;
import by.minilooth.telegrambot.exception.ClientNotFoundException;
import by.minilooth.telegrambot.model.Client;

@Service
public class ClientMessageService extends MessageService {
    
    @Autowired private MessageSender messageSender;
    @Autowired private ClientMessageSource clientMessageSource;

    public void sendStartMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Message message = null;
        Client client = clientBotContext.getClient();

        if (client == null) {
            throw new ClientNotFoundException();
        }

        if (!client.getUser().getLastBotMessageHasReplyMarkup()) {
            message = messageSender.editMessageText(client.getTelegramId(), 
                        clientMessageSource.getMessage("message"), client.getUser().getLastBotMessageId(), null);
        }

        if (message == null) {
            message = messageSender.sendMessage(client.getTelegramId(), 
                        clientMessageSource.getMessage("message"), null); 

            this.updateLastBotMessage(client.getUser(), message);
        }
    }

}
