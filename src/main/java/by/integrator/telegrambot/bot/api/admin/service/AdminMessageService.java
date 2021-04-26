package by.integrator.telegrambot.bot.api.admin.service;

import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.admin.keyboard.inline.AdminInlineKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.admin.keyboard.reply.AdminReplyKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.admin.states.AdminBotContext;
import by.integrator.telegrambot.bot.api.client.keyboard.reply.ClientReplyKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.client.states.ClientBotContext;
import by.integrator.telegrambot.bot.api.client.states.ClientBotState;
import by.integrator.telegrambot.bot.api.enums.UpdateType;
import by.integrator.telegrambot.bot.message.MessageService;
import by.integrator.telegrambot.exception.AdminNotFoundException;
import by.integrator.telegrambot.exception.ClientNotFoundException;
import by.integrator.telegrambot.model.*;
import by.integrator.telegrambot.model.enums.NotificationType;
import by.integrator.telegrambot.service.*;
import by.integrator.telegrambot.util.BotUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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
    private ClientReplyKeyboardMarkupSource clientReplyKeyboardMarkupSource;
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
    @Autowired
    private NotificationService notificationService;

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

    public void sendStartMessage(AdminBotContext adminBotContext) throws AdminNotFoundException {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(admin.getTelegramId(), adminMessageSource.getMessage("message.start"),
                        adminReplyKeyboardMarkupSource.getMainMenuKeyboard());

                updateLastBotMessage(admin.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
            }
        }
    }

    public void sendMainMenuMessage(AdminBotContext adminBotContext) throws AdminNotFoundException {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new AdminNotFoundException();

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
                        adminMessageSource.getMessage("message.notificationAboutNewClient", client.getFirstName(),
                                client.getGoals(), client.getFieldOfActivity(), messengerService.getClientMessengerList(client),
                                client.getIntegrationToCrm(), client.getEmail(), client.getPhoneNumber()), null);
            }
        }
    }

    @SneakyThrows
    public void sendClientListMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        List<Client> clients = clientService.getAll();

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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
        if (admin == null) throw new AdminNotFoundException();

        Client client = admin.getCurrentClient();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(), adminMessageSource.getMessage("message.clientProfile", client.getFirstName(),
                    client.getGoals(), client.getFieldOfActivity(), messengerService.getClientMessengerList(client),
                    client.getIntegrationToCrm(), client.getEmail(), client.getPhoneNumber()),
                    adminInlineKeyboardMarkupSource.getProcessedInlineButton());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendClientProfileMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendInvalidInputMessage(AdminBotContext adminBotContext, String textMessage) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(), "Клиент переведен на этап состаления ТЗ",
                    adminReplyKeyboardMarkupSource.getMainMenuKeyboard());

            updateLastBotMessage(admin.getUser(), message);

            if (admin.getCurrentClient().getClientBotState().toString().equals(ClientBotState.MainMenu.toString())) {
                messageSender.sendMessage(admin.getCurrentClient().getTelegramId(), "Вам доступно составление Технического задания для бота",
                        clientReplyKeyboardMarkupSource.getMainMenuKeyboard(admin.getCurrentClient()));
            }
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
                        adminMessageSource.getMessage("message.newNotificationClientQuestion", client.getFirstName()),
                        adminReplyKeyboardMarkupSource.getMainMenuKeyboard());
            }
        }
    }

    @SneakyThrows
    public void sendClientQuestionsMessage(AdminBotContext adminBotContext) {
        List<Question> questions = questionService.getAllByIsSentAndIsAnswered(true, false);
        System.out.println(questions.size());
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            if (!questions.isEmpty()) {
                for (Question question : questions) {
                    Client client = question.getClient();

                    Message message = messageSender.sendMessage(admin.getTelegramId(),
                            adminMessageSource.getMessage("message.clientQuestion",
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

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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

        if (admin == null) throw new AdminNotFoundException();

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

    @SneakyThrows
    public void sendMenuSettingNotification(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.settingNotification"),
                    adminReplyKeyboardMarkupSource.getMenuNotificationSetting());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendMenuSettingNotification to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendMenuSettingSelectedNotification(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.settingSelectedNotification"),
                    adminReplyKeyboardMarkupSource.getMenuSettingSelectedNotification());

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendMenuSettingSelectedNotification to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendInputNotificationBeforeConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        Integer count = notificationService.getCountByType(NotificationType.BEFORE);
        count++;

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.inputNotificationBeforeConsultation", count),
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendMenuSettingSelectedNotification to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendInputChangedNotificationBeforeConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.inputChangedNotificationBeforeConsultation",
                            adminBotContext.getAdmin().getCurrentNotification().getText()),
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendInputChangedNotificationBeforeConsultation to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendInputChangedNotificationAfterConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.inputChangedNotificationAfterConsultation",
                            adminBotContext.getAdmin().getCurrentNotification().getText()),
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendInputChangedNotificationAfterConsultation to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendChangeNotificationBeforeConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        List<Notification> notifications = notificationService.getAllByType(NotificationType.BEFORE);

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            if (!notifications.isEmpty()) {
                for (Notification notification : notifications) {
                    Message message = messageSender.sendMessage(admin.getTelegramId(),
                            notification.getText(),
                            adminInlineKeyboardMarkupSource.getEditButtonsNotificationMessage(notification));

                    updateLastBotMessage(admin.getUser(), message);
                }
            } else {

            }
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendChangeNotificationBeforeConsultation to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendChangeNotificationAfterConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        List<Notification> notifications = notificationService.getAllByType(NotificationType.AFTER);

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            if (!notifications.isEmpty()) {
                for (Notification notification : notifications) {
                    Message message = messageSender.sendMessage(admin.getTelegramId(),
                            notification.getText(),
                            adminInlineKeyboardMarkupSource.getEditButtonsNotificationMessage(notification));

                    updateLastBotMessage(admin.getUser(), message);
                }
            } else {

            }
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendChangeNotificationAfterConsultation to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendDeleteNotificationBeforeConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        List<Notification> notifications = notificationService.getAllByType(NotificationType.BEFORE);

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            if (!notifications.isEmpty()) {
                for (Notification notification : notifications) {
                    Message message = messageSender.sendMessage(admin.getTelegramId(),
                            notification.getText(),
                            adminInlineKeyboardMarkupSource.getDeleteNotificationButton(notification));

                    updateLastBotMessage(admin.getUser(), message);
                }
            } else {

            }
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendDeleteNotificationBeforeConsultation to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendDeleteNotificationAfterConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        List<Notification> notifications = notificationService.getAllByType(NotificationType.AFTER);

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            if (!notifications.isEmpty()) {
                for (Notification notification : notifications) {
                    Message message = messageSender.sendMessage(admin.getTelegramId(),
                            notification.getText(),
                            adminInlineKeyboardMarkupSource.getDeleteNotificationButton(notification));

                    updateLastBotMessage(admin.getUser(), message);
                }
            } else {

            }
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendDeleteNotificationAfterConsultation to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendInputNotificationAfterConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        Integer count = notificationService.getCountByType(NotificationType.AFTER);
        count++;

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.inputNotificationAfterConsultation", count),
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendInputNotificationAfterConsultation to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendSuccessAddedNotificationBeforeConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        Integer count = notificationService.getCountByType(NotificationType.BEFORE);

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.successAddedNotificationBeforeConsultation", count),
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendSuccessAddedNotificationBeforeConsultation to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendSuccessAddedNotificationAfterConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        Integer count = notificationService.getCountByType(NotificationType.AFTER);

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.successAddedNotificationAfterConsultation", count),
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendSuccessAddedNotificationAfterConsultation to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendSuccessChangedNotificationBeforeConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.successChangedNotificationBeforeConsultation"),
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendSuccessChangedNotificationBeforeConsultation to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendSuccessChangedNotificationAfterConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.successChangedNotificationAfterConsultation"),
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendSuccessChangedNotificationAfterConsultation to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendSuccessDeletedNotificationBeforeConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        List<Notification> notifications = notificationService.getAllByType(NotificationType.BEFORE);

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.successDeletedNotificationBeforeConsultation"),
                    null);

            updateLastBotMessage(admin.getUser(), message);

        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendSuccessDeletedNotificationBeforeConsultation to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public void sendSuccessDeletedNotificationAfterConsultation(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        List<Notification> notifications = notificationService.getAllByType(NotificationType.AFTER);

        if (admin == null) throw new AdminNotFoundException();

        if (checkEditableMessage(adminBotContext)) {
            messageSender.deleteBotLastMessage(admin.getUser());
        }
        try {
            Message message = messageSender.sendMessage(admin.getTelegramId(),
                    adminMessageSource.getMessage("message.successDeletedNotificationAfterConsultation"),
                    null);

            updateLastBotMessage(admin.getUser(), message);
        } catch (TelegramApiException ex) {
            LOGGER.error("Unable to sendSuccessDeletedNotificationAfterConsultation to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
        }
    }
}
