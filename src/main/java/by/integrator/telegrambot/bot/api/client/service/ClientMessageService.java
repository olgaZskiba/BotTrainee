package by.integrator.telegrambot.bot.api.client.service;

import by.integrator.telegrambot.bot.api.client.keyboard.inline.ClientInlineKeyboardMarkupSource;
import by.integrator.telegrambot.model.Messenger;
import by.integrator.telegrambot.service.MessengerService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import by.integrator.telegrambot.bot.api.enums.UpdateType;
import by.integrator.telegrambot.bot.api.client.states.ClientBotContext;
import by.integrator.telegrambot.bot.api.client.keyboard.reply.ClientReplyKeyboardMarkupSource;
import by.integrator.telegrambot.bot.message.MessageService;
import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.exception.ClientNotFoundException;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.util.BotUtils;

import java.util.List;

@Service
public class ClientMessageService extends MessageService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientMessageService.class);

    @Autowired
    private BotUtils botUtils;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private ClientMessageSource clientMessageSource;
    @Autowired
    private ClientReplyKeyboardMarkupSource clientReplyKeyboardMarkupSource;
    @Autowired
    private ClientInlineKeyboardMarkupSource clientInlineKeyboardMarkupSource;
    @Autowired
    private MessengerService messengerService;

    @SneakyThrows
    private Boolean checkCallbackQuery(ClientBotContext clientBotContext) {
        return botUtils.getUpdateType(clientBotContext.getUpdate()).equals(UpdateType.CALLBACK_QUERY) &&
                !clientBotContext.getUpdate().getMessage().getReplyMarkup().equals(new InlineKeyboardMarkup());

    }

    @SneakyThrows
    private Boolean checkEditableMessage(ClientBotContext clientBotContext) {
        Client client = clientBotContext.getClient();
        return client.getUser().hasLastBotMessage() && client.getUser().getBotLastMessageEditable() &&
                !messageSender.isMessageExpired(client.getUser().getBotLastMessageDate());
    }

    public void sendStartMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.start"), null);

                updateLastBotMessage(client.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
            }
        }
    }

    public void sendAboutBotMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.aboutBot"),
                        null);

                updateLastBotMessage(client.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
            }
        }
    }

    public void sendAnonsOneMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.anonsOne"),
                        null);

                updateLastBotMessage(client.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
            }
        }
    }

    public void sendMainMenuMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.mainMenu"),
                        clientReplyKeyboardMarkupSource.getMainMenuKeyboard());

                updateLastBotMessage(client.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
            }
        }
    }

    public void sendWhatIsTheBotMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.WhatIsTheBot"),
                        null);

                updateLastBotMessage(client.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
            }
        }
    }

    public void sendWhatCanBotMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.WhatIsTheBot"),
                        null);

                updateLastBotMessage(client.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to sendWhatCanBotMessage to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
            }
        }
    }

    @SneakyThrows
    public void sendInputFirstNameMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {
            messageSender.deleteBotLastMessage(client.getUser());
        }
        try {
            Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.firstName"),
                    null);

            updateLastBotMessage(client.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendInputFirstNameMessage to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendInputLastNameMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {
            messageSender.deleteBotLastMessage(client.getUser());
        }
        try {
            Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.lastName"),
                    null);

            updateLastBotMessage(client.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendInputEmailMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {
            messageSender.deleteBotLastMessage(client.getUser());
        }
        try {
            ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
            replyKeyboardRemove.setRemoveKeyboard(true);
            Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.email"),
                    replyKeyboardRemove);
            updateLastBotMessage(client.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
        }
    }

    public void sendInputPhoneNumberMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.phoneNumber"),
                        clientReplyKeyboardMarkupSource.getPhoneNumberKeyboard());

                updateLastBotMessage(client.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
            }
        }
    }

    @SneakyThrows
    public void sendSelectBotTypeMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();
        List<Messenger> messengers = messengerService.getAll();
        List<Messenger> selectedMessengers = messengerService.getAllByClient(client);
        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {
            Message message = messageSender.editMessageReplyMarkup2(client.getTelegramId(),
                    client.getUser().getBotLastMessageId(),
                    clientInlineKeyboardMarkupSource.getListBotTypes(selectedMessengers, messengers));
            updateLastBotMessage(client.getUser(), message);
        } else {
            try {
                Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.botType"),
                        clientInlineKeyboardMarkupSource.getListBotTypes(selectedMessengers, messengers));

                updateLastBotMessage(client.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
            }
        }
    }


    @SneakyThrows
    public void sendProfileMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {
            Message message = messageSender.editMessageText(client.getTelegramId(),
                    clientMessageSource.getMessage("message.profile", client.getLastName(), client.getFirstName(),
                            client.getEmail(), client.getPhoneNumber(), messengerService.getClientMessengerList(client)),
                    client.getUser().getBotLastMessageId(), clientInlineKeyboardMarkupSource.getConfirmInlineKeyboard());
            updateLastBotMessage(client.getUser(), message);
        } else {
            try {
                Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.profile", client.getLastName(), client.getFirstName(),
                        client.getEmail(), client.getPhoneNumber(), messengerService.getClientMessengerList(client)),
                        clientInlineKeyboardMarkupSource.getConfirmInlineKeyboard());

                updateLastBotMessage(client.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
            }
        }
    }

    @SneakyThrows
    public void sendFinishFilledProfileMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {
            messageSender.deleteMessage(client.getTelegramId(), client.getUser().getBotLastMessageId());
        }
        try {
            Message message = messageSender.sendMessage(client.getTelegramId(),
                    clientMessageSource.getMessage("message.finishRegistration"),
                    null);

            updateLastBotMessage(client.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
        }

    }

    public void sendAskQuestionMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.askQuestion"),
                        clientReplyKeyboardMarkupSource.getSendQuestionKeyboard());

                updateLastBotMessage(client.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
            }
        }
    }

    public void sendWebsiteMessage(ClientBotContext clientBotContext) throws ClientNotFoundException {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(client.getTelegramId(), clientMessageSource.getMessage("message.website"),
                        null);

                updateLastBotMessage(client.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
            }
        }
    }

    @SneakyThrows
    public void sendInvalidInputMessage(ClientBotContext clientBotContext, String textMessage) {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(client.getTelegramId(), textMessage, null);

                updateLastBotMessage(client.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
            }
        }
    }

    @SneakyThrows
    public void sendSelectFieldForChangeMessage(ClientBotContext clientBotContext) {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {
            Message message = messageSender.editMessageText(client.getTelegramId(),
                    clientMessageSource.getMessage("message.selectFieldForChange"), client.getUser().getBotLastMessageId(),
                    clientInlineKeyboardMarkupSource.getClientFieldsInlineKeyboard());
            updateLastBotMessage(client.getUser(), message);
        } else {
            try {
                Message message = messageSender.sendMessage(client.getTelegramId(),
                        clientMessageSource.getMessage("message.selectFieldForChange"), clientInlineKeyboardMarkupSource.getClientFieldsInlineKeyboard());

                updateLastBotMessage(client.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: {0}, reason: {1}", client.getTelegramId(), ex.getLocalizedMessage());
            }
        }
    }

    @SneakyThrows
    public void sendSomethingWentWrongMessage(ClientBotContext clientBotContext) {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {
            messageSender.deleteBotLastMessage(client.getUser());
        }
        try {
            Message message = messageSender.sendMessage(client.getTelegramId(),
                    clientMessageSource.getMessage("message.somethingWentWrong"), null);

            updateLastBotMessage(client.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendSomethingWentWrongMessage to user: " + client.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendCancelSendingQuestionMessage(ClientBotContext clientBotContext) {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {
            messageSender.deleteBotLastMessage(client.getUser());
        }
        try {
            Message message = messageSender.sendMessage(client.getTelegramId(),
                    clientMessageSource.getMessage("message.cancelSendingQuestion"), null);

            updateLastBotMessage(client.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendCancelSendingQuestionMessage to user: " + client.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendSendingQuestionMessage(ClientBotContext clientBotContext) {
        Client client = clientBotContext.getClient();

        if (client == null) throw new ClientNotFoundException();

        if (checkEditableMessage(clientBotContext)) {
            messageSender.deleteBotLastMessage(client.getUser());
        }
        try {
            Message message = messageSender.sendMessage(client.getTelegramId(),
                    clientMessageSource.getMessage("message.sentSendingQuestion"), null);

            updateLastBotMessage(client.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendSendingQuestionMessage to user: " + client.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }
}
