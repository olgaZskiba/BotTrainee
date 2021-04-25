package by.integrator.telegrambot.bot.api.admin.service;

import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.admin.keyboard.inline.AdminInlineKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.admin.keyboard.reply.AdminReplyKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.admin.states.AdminBotContext;
import by.integrator.telegrambot.bot.api.client.states.ClientBotContext;
import by.integrator.telegrambot.bot.api.enums.UpdateType;
import by.integrator.telegrambot.bot.message.MessageService;
import by.integrator.telegrambot.exception.ClientNotFoundException;
import by.integrator.telegrambot.model.*;
import by.integrator.telegrambot.service.*;
import by.integrator.telegrambot.util.BotUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class AdminMessageService extends MessageService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AdminMessageService.class);

    @Autowired
    private BotUtils botUtils;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private AdminMessageSource adminMessageSource;
    @Autowired
    private AdminReplyKeyboardMarkupSource adminReplyKeyboardMarkupSource;
    @Autowired
    private AdminInlineKeyboardMarkupSource adminInlineKeyboardMarkupSource;
    @Autowired
    private MessengerService messengerService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private PostponeMessageService postponeMessageService;

    @SneakyThrows
    private Boolean checkCallbackQuery(AdminBotContext clientBotContext) {
        return botUtils.getUpdateType(clientBotContext.getUpdate()).equals(UpdateType.CALLBACK_QUERY) &&
                !clientBotContext.getUpdate().getMessage().getReplyMarkup().equals(new InlineKeyboardMarkup());

    }

    @SneakyThrows
    private Boolean checkEditableMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();
        return admin.getUser().hasLastBotMessage() && admin.getUser().getBotLastMessageEditable() &&
                !messageSender.isMessageExpired(admin.getUser().getBotLastMessageDate());
    }

    public void sendStartMessage(AdminBotContext adminBotContext) throws ClientNotFoundException {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(admin.getTelegramId(), adminMessageSource.getMessage("message.start"),
                        null);

                updateLastBotMessage(admin.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
            }
        }
    }

    public void sendMainMenuMessage(AdminBotContext adminBotContext) throws ClientNotFoundException {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(), adminMessageSource.getMessage("message.mainMenu"),
                    adminReplyKeyboardMarkupSource.getMainMenuKeyboard());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to send start message to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }

    }

    @SneakyThrows
    public void sendNotificationAboutNewClient(ClientBotContext botContext) {
        List<Admin> admins = adminService.getAll();
        if (!admins.isEmpty()) {
            Client client = botContext.getClient();
            for (Admin admin : admins) {
                messageSender.sendMessage(admin.getTelegramId(),
                        adminMessageSource.getMessage("message.notificationAboutNewClient", client.getLastName(), client.getFirstName(),
                                client.getEmail(), client.getPhoneNumber(), messengerService.getClientMessengerList(client)), null);
            }
        }
    }

    @SneakyThrows
    public void sendClientListMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        List<Client> clients = clientService.getAll();

        if (admin == null) throw new ClientNotFoundException();

        if (!clients.isEmpty()) {
            try {
                Message message = messageSender.sendMessage(admin.getTelegramId(), "Клиенты: ",
                        adminInlineKeyboardMarkupSource.generateClientsMultiplySelectablePageableInlineMarkup(clients,
                                adminBotContext.getAdmin().getUser().getCurrentPage()));

                updateLastBotMessage(admin.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to sendClientListMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
            }
        } else {
            try {
                Message message = messageSender.sendMessage(admin.getTelegramId(), adminMessageSource.getMessage("message.clientListIsEmpty"),
                        adminReplyKeyboardMarkupSource.getMainMenuKeyboard());

                updateLastBotMessage(admin.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to sendClientListMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
            }
        }
    }

    @SneakyThrows
    public void sendClientNotFoundMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(), adminMessageSource.getMessage("message.clientNotFound"),
                    adminReplyKeyboardMarkupSource.getMainMenuKeyboard());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendClientNotFoundMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }

    }

    @SneakyThrows
    public void sendClientProfileMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();
        if (admin == null) throw new ClientNotFoundException();

        Client client = admin.getCurrentClient();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(), adminMessageSource.getMessage("message.clientProfile", client.getLastName(), client.getFirstName(),
                    client.getEmail(), client.getPhoneNumber(), messengerService.getClientMessengerList(client)),
                    adminInlineKeyboardMarkupSource.getProcessedInlineButton());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendClientProfileMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendInvalidInputMessage(AdminBotContext adminBotContext, String textMessage) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(), textMessage, null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendClientNotFoundMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendInputFirstNameMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(), "Введите ваше имя: ", null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendInputFirstNameMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendClientProcessedMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(), "Бриф отправлен",
                    adminReplyKeyboardMarkupSource.getMainMenuKeyboard());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendInputFirstNameMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendNotificationAboutNewClientQuestion(ClientBotContext clientBotContext) {
        List<Admin> admins = adminService.getAll();
        if (!admins.isEmpty()) {
            Client client = clientBotContext.getClient();
            for (Admin admin : admins) {
                messageSender.sendMessage(admin.getTelegramId(),
                        adminMessageSource.getMessage("message.newNotificationClientQuestion",
                                client.getLastName(), client.getFirstName()),
                        adminReplyKeyboardMarkupSource.getMainMenuKeyboard());
            }
        }
    }

    @SneakyThrows
    public void sendClientQuestionsMessage(AdminBotContext adminBotContext) {
        List<Question> questions = questionService.getAllByIsSentAndIsAnswered(true, false);

        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            if (!questions.isEmpty()) {
                for (Question question : questions) {
                    Client client = question.getClient();

                    Message message = messageSender.sendMessage(admin.getTelegramId(),
                            adminMessageSource.getMessage("message.clientQuestion",
                                    client.getLastName(),
                                    client.getFirstName(), question.getQuestion()),
                            adminInlineKeyboardMarkupSource.getAnswerTheQuestionInlineButton(client, question));

                    updateLastBotMessage(admin.getUser(), message);

                }
            } else {
                Message message = messageSender.sendMessage(admin.getTelegramId(),
                        adminMessageSource.getMessage("message.questionListIsEmpty"),
                        null);

                updateLastBotMessage(admin.getUser(), message);
            }
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendClientQuestionsMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }

    }

    @SneakyThrows
    public void sendInputAnswerOnClientQuestionMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.InputAnswerOnClientQuestion",
                            admin.getCurrentQuestion().getQuestion()),
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendInputFirstNameMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendAnswerToClient(AdminBotContext adminBotContext, String userAnswer) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            messageSender.sendMessage(admin.getCurrentClient().getTelegramId(),
                    adminMessageSource.getMessage("message.answerToClient", admin.getFirstName(),
                            admin.getCurrentQuestion().getQuestion(),
                            userAnswer),
                    null);

            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.questionAnswered"),
                    adminReplyKeyboardMarkupSource.getMainMenuKeyboard());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendAnswerToClient to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendSettingsPostponeMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.selectActionWithPostponeMessage"),
                    adminReplyKeyboardMarkupSource.getButtonsPostponeMessageMenu());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendSettingsPostponeMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendListPostponeMessages(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        List<PostponeMessage> postponeMessages = postponeMessageService.getAllWithoutLast();
        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            if (!postponeMessages.isEmpty()) {
                for (int i = 0; i < postponeMessages.size(); i++) {
                    Message message = messageSender.sendMessage(admin.getTelegramId(),
                            adminMessageSource.getMessage("reply.postponeMessage.selectNumberForRemoving", postponeMessages.get(i).getText(),
                                    postponeMessages.get(i).getDate()),
                            adminInlineKeyboardMarkupSource.getListButtonsPostponeMessage(postponeMessages.get(i)));

                    updateLastBotMessage(admin.getUser(), message);
                }
            } else {
                Message message = messageSender.sendMessage(admin.getTelegramId(),
                        adminMessageSource.getMessage("reply.postponeMessage.listPostponeMessageIsEmpty"),
                        adminReplyKeyboardMarkupSource.getButtonsPostponeMessageMenu());
                updateLastBotMessage(admin.getUser(), message);
            }
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendListPostponeMessages to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }

    }

    @SneakyThrows
    public void sendPostponeMessageRemoved(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("reply.postponeMessage.removed"),
                    adminReplyKeyboardMarkupSource.getButtonsPostponeMessageMenu());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendPostponeMessageRemoved to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendPostponeMessageNotFound(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("reply.postponeMessage.notFound"),
                    adminReplyKeyboardMarkupSource.getButtonsPostponeMessageMenu());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendPostponeMessageNotFound to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendAddPostponeMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("reply.postponeMessage.enterText"),
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendAddPostponeMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendLoadPictureToPostponeMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("reply.postponeMessage.uploadPhoto"),
                    adminInlineKeyboardMarkupSource.getButtonsCancelLoadPicture());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendLoadPictureToPostponeMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendEnterDayAndTimePostponeMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("reply.postponeMessage.setDateAndTime"),
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendEnterDayAndTimePostponeMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendInvalidDateOrTimeMessage(AdminBotContext adminBotContext, String messageText) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    messageText,
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendInvalidDateOrTimeMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendConfirmPostponeMessageKeyboard(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("reply.ConfirmPostponeMessageKeyboard"),
                    adminReplyKeyboardMarkupSource.getConfirmPostponeMessageKeyboard());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendConfirmPostponeMessageKeyboard to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendSuccessPostponeMessageConfirmationMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("reply.SuccessPostponeMessageConfirmationMessage"),
                    adminReplyKeyboardMarkupSource.getMainMenuKeyboard());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendSuccessPostponeMessageConfirmationMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendDeclinePostponeMessageConfirmationMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("reply.DeclinePostponeMessageConfirmationMessage"),
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendSuccessPostponeMessageConfirmationMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }
}
