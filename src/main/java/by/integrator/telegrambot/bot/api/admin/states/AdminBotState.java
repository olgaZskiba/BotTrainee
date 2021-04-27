package by.integrator.telegrambot.bot.api.admin.states;

import by.integrator.telegrambot.bot.api.BotState;
import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.admin.keyboard.reply.AdminReplyKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.admin.service.AdminMessageService;
import by.integrator.telegrambot.bot.keyboard.InlineKeyboardMarkupSource;
import by.integrator.telegrambot.exception.AdminBotStateException;
import by.integrator.telegrambot.model.*;
import by.integrator.telegrambot.model.enums.NotificationType;
import by.integrator.telegrambot.service.*;
import by.integrator.telegrambot.validation.ValidationResult;
import by.integrator.telegrambot.validation.Validator;
import com.vdurmont.emoji.EmojiParser;
import lombok.Setter;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public enum AdminBotState implements BotState<AdminBotState, AdminBotContext> {
    Start(false) {
        private AdminBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendStartMessage(botContext);
            Admin admin = botContext.getAdmin();
            admin.setFirstName(botContext.getUpdate().getMessage().getFrom().getFirstName());
        }

        @Override
        public AdminBotState nextState() {
            return MainMenu;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    MainMenu(true) {
        private AdminBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendMainMenuMessage(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            String userAnswer = botContext.getUpdate().getMessage().getText();
            if (AdminReplyKeyboardMarkupSource.CLIENTS.equals(userAnswer)) {
                nextState = ClientList;
            } else if (AdminReplyKeyboardMarkupSource.POSTPONE.equals(userAnswer)) {
                nextState = SettingsPostponeMessage;
            } else if (AdminReplyKeyboardMarkupSource.SETTING_TEXT.equals(userAnswer)) {
                nextState = SelectTypeNotification;
            } else if (userAnswer.startsWith(AdminReplyKeyboardMarkupSource.QUESTIONS)) {
                nextState = ClientQuestions;
            } else {
                nextState = MainMenu;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    ClientList(true) {
        private AdminBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendClientListMessage(botContext);
        }

        @SneakyThrows
        @Override
        public void handleText(AdminBotContext botContext) {
            String userAnswer = botContext.getUpdate().getMessage().getText();
            if (AdminReplyKeyboardMarkupSource.CLIENTS.equals(userAnswer)) {
                nextState = ClientList;
            } else if (AdminReplyKeyboardMarkupSource.POSTPONE.equals(userAnswer)) {
                nextState = SettingsPostponeMessage;
            } else if (AdminReplyKeyboardMarkupSource.SETTING_TEXT.equals(userAnswer)) {
                nextState = SelectTypeNotification;
            } else if (userAnswer.startsWith(AdminReplyKeyboardMarkupSource.QUESTIONS)) {
                nextState = ClientQuestions;
            } else {
                nextState = MainMenu;
            }
        }

        @Override
        public void handleCallbackQuery(AdminBotContext adminBotContext) {
            String callbackData = EmojiParser.removeAllEmojis(adminBotContext.getUpdate().getCallbackQuery().getData());

            Integer countOfClients = clientService.countAllClients();
            Integer page = adminBotContext.getAdmin().getUser().getCurrentPage();

            if (callbackData.equals("callback.previous")) {
                if (page > 1) {
                    page--;
                } else {
                    page = (int) Math.ceil((float) countOfClients / (float) InlineKeyboardMarkupSource.ITEMS_PER_PAGE);
                }

                adminBotContext.getAdmin().getUser().setCurrentPage(page);
                nextState = ClientList;
            } else if (callbackData.equals("callback.next")) {
                if (page * InlineKeyboardMarkupSource.ITEMS_PER_PAGE < countOfClients) {
                    page++;
                } else {
                    page = 1;
                }

                adminBotContext.getAdmin().getUser().setCurrentPage(page);
                nextState = ClientList;
            } else {
                adminBotContext.getAdmin().getUser().setCurrentPage(UserService.DEFAULT_PAGE);
                try {
                    Client client = clientService.getById(Integer.parseInt(callbackData));
                    if (client != null) {
                        adminBotContext.getAdmin().setCurrentClient(client);
                        nextState = ClientProfile;
                    } else {
                        adminMessageService.sendClientNotFoundMessage(adminBotContext);
                    }
                } catch (NumberFormatException ex) {
                    adminMessageService.sendClientNotFoundMessage(adminBotContext);
                } finally {
                    nextState = ClientProfile;
                }
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    ClientProfile(true) {
        private AdminBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendClientProfileMessage(botContext);
        }

        @SneakyThrows
        @Override
        public void handleText(AdminBotContext botContext) {
            String userAnswer = botContext.getUpdate().getMessage().getText();
            if (AdminReplyKeyboardMarkupSource.CLIENTS.equals(userAnswer)) {
                nextState = ClientList;
            } else if (AdminReplyKeyboardMarkupSource.POSTPONE.equals(userAnswer)) {
                nextState = SettingsPostponeMessage;
            } else if (AdminReplyKeyboardMarkupSource.SETTING_TEXT.equals(userAnswer)) {
                nextState = SelectTypeNotification;
            } else if (userAnswer.startsWith(AdminReplyKeyboardMarkupSource.QUESTIONS)) {
                nextState = ClientQuestions;
            } else {
                nextState = MainMenu;
            }
        }

        @Override
        public void handleCallbackQuery(AdminBotContext adminBotContext) {
            String callbackData = adminBotContext.getUpdate().getCallbackQuery().getData();

            switch (callbackData) {
                case "callback.processed":
                    nextState = ClientProcessed;
                    break;
                default:
                    nextState = MainMenu;
                    break;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    ClientProcessed(false) {
        private AdminBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendClientProcessedMessage(botContext);
            botContext.getAdmin().setCurrentClient(null);
        }

        @Override
        public AdminBotState nextState() {
            return MainMenu;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    ClientQuestions(true) {
        private AdminBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendClientQuestionsMessage(botContext);
        }

        @Override
        public void handleCallbackQuery(AdminBotContext adminBotContext) {
            String callbackData = adminBotContext.getUpdate().getCallbackQuery().getData();
            String[] parseData = callbackData.substring(16).split("\\.");

            int clientId = Integer.parseInt(parseData[0]);
            int questionId = Integer.parseInt(parseData[1]);

            Client client = clientService.getById(clientId);
            if (client != null) {
                Question question = questionService.getById(questionId);
                adminBotContext.getAdmin().setCurrentClient(client);
                adminBotContext.getAdmin().setCurrentQuestion(question);
                nextState = InputAnswerOnClientQuestion;
            } else {
                adminMessageService.sendClientNotFoundMessage(adminBotContext);
                nextState = MainMenu;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    InputAnswerOnClientQuestion(true) {
        private AdminBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendInputAnswerOnClientQuestionMessage(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            String userAnswer = botContext.getUpdate().getMessage().getText();
            adminMessageService.sendAnswerToClient(botContext, userAnswer);

            Question question = botContext.getAdmin().getCurrentQuestion();
            question.setIsAnswered(true);
            questionService.save(question);

            botContext.getAdmin().setCurrentClient(null);
            botContext.getAdmin().setCurrentQuestion(null);
        }

        @Override
        public AdminBotState nextState() {
            return MainMenu;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SettingsPostponeMessage(true) {
        AdminBotState nextState = null;

        @Override
        public void enter(AdminBotContext adminBotContext) {
            adminMessageService.sendSettingsPostponeMessage(adminBotContext);
        }

        @Override
        public void handleText(AdminBotContext adminBotContext) {
            String text = adminBotContext.getUpdate().getMessage().getText();
            switch (text) {
                case "Главное меню":
                    nextState = MainMenu;
                    break;
                case "Добавление сообщений":
                    nextState = AddPostponeMessage;
                    break;
                case "Удаление сообщений":
                    nextState = DeletePostponeMessage;
                    break;
                default:
                    nextState = SettingsPostponeMessage;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    AddPostponeMessage(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext adminBotContext) {
            adminMessageService.sendAddPostponeMessage(adminBotContext);
        }

        @Override
        public void handleText(AdminBotContext adminBotContext) {
            String text = adminBotContext.getUpdate().getMessage().getText();
            PostponeMessage postponeMessage = postponeMessageService.getLastByChatId(adminBotContext.
                    getAdmin().getTelegramId());
            if (postponeMessage != null) {
                postponeMessage.setText(text);
                postponeMessageService.save(postponeMessage);
            } else {
                PostponeMessage newPostponeMessage = PostponeMessage.builder()
                        .text(text)
                        .isLast(true)
                        .chatId(adminBotContext.getAdmin().getTelegramId())
                        .build();
                postponeMessageService.save(newPostponeMessage);
            }
            nextState = AddPicture;
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    AddPicture(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext adminBotContext) {
            adminMessageService.sendLoadPictureToPostponeMessage(adminBotContext);
        }

        @Override
        public void handleText(AdminBotContext adminBotContext) {
            nextState = AddPicture;
        }

        @Override
        public void handleCallbackQuery(AdminBotContext adminBotContext) {
            String text = adminBotContext.getUpdate().getCallbackQuery().getData();
            if (text.equals("buttonCancelLoadPicture")) {
                nextState = SetDayAndTime;
            } else {
                nextState = AddPicture;
            }
        }

        @Override
        public void handlePhoto(AdminBotContext adminBotContext) {
            final List<PhotoSize> photos = adminBotContext.getUpdate().getMessage().getPhoto();
            String fileId = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null)).getFileId();
            PostponeMessage postponeMessage = postponeMessageService.getLastByChatId(adminBotContext.
                    getAdmin().getTelegramId());
            if (postponeMessage != null) {
                postponeMessage.setPictureUrl(fileId);
                postponeMessageService.save(postponeMessage);
                nextState = SetDayAndTime;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SetDayAndTime(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext adminBotContext) {
            adminMessageService.sendEnterDayAndTimePostponeMessage(adminBotContext);
        }

        @Override
        public void handleText(AdminBotContext adminBotContext) {
            String text = adminBotContext.getUpdate().getMessage().getText();
            PostponeMessage postponedMessage = postponeMessageService.getLastByChatId(adminBotContext.
                    getAdmin().getUser().getTelegramId());
            ValidationResult validationResult = null;
            validationResult = Validator.validateDate(text);
            if (!validationResult.getIsValid()) {
                adminMessageService.sendInvalidDateOrTimeMessage(adminBotContext, validationResult.getMessage());
                nextState = SetDayAndTime;
            } else {
                try {
                    Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(text);
                    postponedMessage.setDate(date);

                    postponeMessageService.save(postponedMessage);

                    nextState = ConfirmationForSendMessage;
                } catch (ParseException e) {
                    nextState = SetDayAndTime;
                }
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    ConfirmationForSendMessage(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext adminBotContext) {
            adminMessageService.sendConfirmPostponeMessageKeyboard(adminBotContext);
        }

        @Override
        public void handleText(AdminBotContext adminBotContext) {
            String text = adminBotContext.getUpdate().getMessage().getText();
            PostponeMessage postponeMessage = postponeMessageService.getLastByChatId(adminBotContext
                    .getAdmin().getUser().getTelegramId());

            switch (text) {
                case "Да":
                    postponeMessage.setIsLast(false);
                    postponeMessageService.save(postponeMessage);
                    nextState = SuccessPostponeMessageConfirmation;
                    break;
                case "Нет":
                    postponeMessageService.delete(postponeMessage);
                    nextState = DeclinePostponeMessageConfirmation;
                    break;
                case "Назад":
                    postponeMessageService.delete(postponeMessage);
                    nextState = SettingsPostponeMessage;
                    break;
                default:
                    nextState = ConfirmationForSendMessage;
                    break;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    DeclinePostponeMessageConfirmation(false) {
        @Override
        public void enter(AdminBotContext adminBotContext) {
            adminMessageService.sendDeclinePostponeMessageConfirmationMessage(adminBotContext);
        }

        @Override
        public AdminBotState nextState() {
            return SettingsPostponeMessage;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SuccessPostponeMessageConfirmation(true) {
        @Override
        public void enter(AdminBotContext adminBotContext) {
            adminMessageService.sendSuccessPostponeMessageConfirmationMessage(adminBotContext);
        }

        @Override
        public AdminBotState nextState() {
            return SettingsPostponeMessage;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    DeletePostponeMessage(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendListPostponeMessages(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            nextState = SettingsPostponeMessage;
        }

        @SneakyThrows
        @Override
        public void handleCallbackQuery(AdminBotContext botContext) {
            String callbackData = botContext.getUpdate().getCallbackQuery().getData();
            PostponeMessage postponeMessage = postponeMessageService
                    .getById(Integer.parseInt(callbackData));
            if (postponeMessage != null) {
                postponeMessageService.delete(postponeMessage);
                adminMessageService.sendPostponeMessageRemoved(botContext);
            } else {
                adminMessageService.sendPostponeMessageNotFound(botContext);
            }
            nextState = SettingsPostponeMessage;
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SettingNotificationBeforeConsultation(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendMenuSettingSelectedNotification(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            switch (botContext.getUpdate().getMessage().getText()) {
                case "Добавить":
                    nextState = InputNotificationBeforeConsultation;
                    break;
                case "Изменить":
                    nextState = SelectForChangeNotificationBeforeConsultation;
                    break;
                case "Удалить":
                    nextState = SelectForDeleteNotificationBeforeConsultation;
                    break;
                case "Назад":
                    nextState = SelectTypeNotification;
                    break;
                default:
                    nextState = MainMenu;
                    break;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SettingNotificationAfterConsultation(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendMenuSettingSelectedNotification(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            switch (botContext.getUpdate().getMessage().getText()) {
                case "Добавить":
                    nextState = InputNotificationAfterConsultation;
                    break;
                case "Изменить":
                    nextState = SelectForChangeNotificationAfterConsultation;
                    break;
                case "Удалить":
                    nextState = SelectForDeleteNotificationAfterConsultation;
                    break;
                case "Назад":
                    nextState = SelectTypeNotification;
                    break;
                default:
                    nextState = MainMenu;
                    break;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SelectForChangeNotificationBeforeConsultation(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendChangeNotificationBeforeConsultation(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            switch (botContext.getUpdate().getMessage().getText()) {
                case "Добавить":
                    nextState = InputNotificationBeforeConsultation;
                    break;
                case "Изменить":
                    nextState = SelectForChangeNotificationBeforeConsultation;
                    break;
                case "Удалить":
                    nextState = SelectForDeleteNotificationBeforeConsultation;
                    break;
                case "Назад":
                    nextState = SelectTypeNotification;
                    break;
                default:
                    nextState = MainMenu;
                    break;
            }
        }

        @Override
        public void handleCallbackQuery(AdminBotContext adminBotContext) {
            String data = adminBotContext.getUpdate().getCallbackQuery().getData();
            int notificationId = Integer.parseInt(data);
            Notification notification = notificationService.getById(notificationId);
            if (notification != null) {
                adminBotContext.getAdmin().setCurrentNotification(notification);
                nextState = InputChangedNotificationBeforeConsultation;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SelectForDeleteNotificationBeforeConsultation(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendDeleteNotificationBeforeConsultation(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            switch (botContext.getUpdate().getMessage().getText()) {
                case "Добавить":
                    nextState = InputNotificationBeforeConsultation;
                    break;
                case "Изменить":
                    nextState = SelectForChangeNotificationBeforeConsultation;
                    break;
                case "Удалить":
                    nextState = SelectForDeleteNotificationBeforeConsultation;
                    break;
                case "Назад":
                    nextState = SelectTypeNotification;
                    break;
                default:
                    nextState = MainMenu;
                    break;
            }
        }

        @Override
        public void handleCallbackQuery(AdminBotContext adminBotContext) {
            String data = adminBotContext.getUpdate().getCallbackQuery().getData();
            int notificationId = Integer.parseInt(data);
            Notification notification = notificationService.getById(notificationId);
            if (notification != null) {
                notificationService.delete(notification);
                nextState = SuccessDeletedNotificationAfterConsultation;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SelectForChangeNotificationAfterConsultation(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendChangeNotificationAfterConsultation(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            switch (botContext.getUpdate().getMessage().getText()) {
                case "Добавить":
                    nextState = InputNotificationAfterConsultation;
                    break;
                case "Изменить":
                    nextState = SelectForChangeNotificationAfterConsultation;
                    break;
                case "Удалить":
                    nextState = SelectForDeleteNotificationAfterConsultation;
                    break;
                case "Назад":
                    nextState = SelectTypeNotification;
                    break;
                default:
                    nextState = MainMenu;
                    break;
            }
        }

        @Override
        public void handleCallbackQuery(AdminBotContext adminBotContext) {
            String data = adminBotContext.getUpdate().getCallbackQuery().getData();
            int notificationId = Integer.parseInt(data);
            Notification notification = notificationService.getById(notificationId);
            if (notification != null) {
                adminBotContext.getAdmin().setCurrentNotification(notification);
                nextState = InputChangedNotificationAfterConsultation;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SelectForDeleteNotificationAfterConsultation(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendDeleteNotificationAfterConsultation(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            switch (botContext.getUpdate().getMessage().getText()) {
                case "Добавить":
                    nextState = InputNotificationAfterConsultation;
                    break;
                case "Изменить":
                    nextState = SelectForChangeNotificationAfterConsultation;
                    break;
                case "Удалить":
                    nextState = SelectForDeleteNotificationAfterConsultation;
                    break;
                case "Назад":
                    nextState = SelectTypeNotification;
                    break;
                default:
                    nextState = MainMenu;
                    break;
            }
        }

        @Override
        public void handleCallbackQuery(AdminBotContext adminBotContext) {
            String data = adminBotContext.getUpdate().getCallbackQuery().getData();
            int notificationId = Integer.parseInt(data);
            Notification notification = notificationService.getById(notificationId);
            if (notification != null) {
                notificationService.delete(notification);
                nextState = SuccessDeletedNotificationAfterConsultation;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    InputChangedNotificationBeforeConsultation(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendInputChangedNotificationBeforeConsultation(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            Notification notification = botContext.getAdmin().getCurrentNotification();
            notification.setText(botContext.getUpdate().getMessage().getText());
            notificationService.save(notification);
        }

        @Override
        public AdminBotState nextState() {
            return SuccessChangedNotificationBeforeConsultation;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    InputChangedNotificationAfterConsultation(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendInputChangedNotificationAfterConsultation(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            Notification notification = botContext.getAdmin().getCurrentNotification();
            notification.setText(botContext.getUpdate().getMessage().getText());
            notificationService.save(notification);
        }

        @Override
        public AdminBotState nextState() {
            return SuccessChangedNotificationAfterConsultation;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    InputNotificationBeforeConsultation(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendInputNotificationBeforeConsultation(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            notificationService.createNotification(botContext.getUpdate().getMessage().getText(),
                    NotificationType.BEFORE);
        }

        @Override
        public AdminBotState nextState() {
            return SuccessAddedNotificationBeforeConsultation;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    InputNotificationAfterConsultation(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendInputNotificationAfterConsultation(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            notificationService.createNotification(botContext.getUpdate().getMessage().getText(),
                    NotificationType.AFTER);
        }

        @Override
        public AdminBotState nextState() {
            return SuccessAddedNotificationAfterConsultation;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SuccessAddedNotificationBeforeConsultation(false) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendSuccessAddedNotificationBeforeConsultation(botContext);
        }

        @Override
        public AdminBotState nextState() {
            return SelectTypeNotification;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SuccessChangedNotificationBeforeConsultation(false) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendSuccessChangedNotificationBeforeConsultation(botContext);
            botContext.getAdmin().setCurrentNotification(null);
        }

        @Override
        public AdminBotState nextState() {
            return SelectTypeNotification;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SuccessChangedNotificationAfterConsultation(false) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendSuccessChangedNotificationAfterConsultation(botContext);
            botContext.getAdmin().setCurrentNotification(null);
        }

        @Override
        public AdminBotState nextState() {
            return SelectTypeNotification;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SuccessDeletedNotificationBeforeConsultation(false) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendSuccessDeletedNotificationBeforeConsultation(botContext);
        }

        @Override
        public AdminBotState nextState() {
            return SelectTypeNotification;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SuccessDeletedNotificationAfterConsultation(false) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendSuccessDeletedNotificationAfterConsultation(botContext);
        }

        @Override
        public AdminBotState nextState() {
            return SelectTypeNotification;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SuccessAddedNotificationAfterConsultation(false) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendSuccessAddedNotificationAfterConsultation(botContext);
        }

        @Override
        public AdminBotState nextState() {
            return SelectTypeNotification;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SuccessSaveText(false) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendSuccessSavedText(botContext);
        }

        @Override
        public AdminBotState nextState() {
            return SelectTypeNotification;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SettingWhatCanBotText(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendMenuSettingSelectedText(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            switch (botContext.getUpdate().getMessage().getText()) {
                case "Добавить":
                    List<Notification> notification =
                            notificationService.getAllByType(NotificationType.WHAT_CAN_BOT);
                    if (notification.isEmpty()) {
                        nextState = InputWhatCanBotText;
                    } else {
                        nextState = FoundCurrentText;
                    }
                    break;
                case "Изменить":
                    nextState = ChangeTextWhatCanBot;
                    break;
                case "Назад":
                    nextState = SelectTypeNotification;
                    break;
                default:
                    nextState = MainMenu;
                    break;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    ChangeTextWhatCanBot(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext adminBotContext) {
            adminMessageService.sendChangeTextWhatCanBotText(adminBotContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            String text = botContext.getUpdate().getMessage().getText();
            List<Notification> notification = notificationService.getAllByType(NotificationType.WHAT_CAN_BOT);
            if (!notification.isEmpty()) {
                Notification notificationForChange = notification.get(0);
                notificationForChange.setText(text);
                notificationService.save(notificationForChange);
                nextState = SuccessSaveText;
            } else {
                nextState = SelectTypeNotification;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    InputWhatCanBotText(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendInputWhatCanBotText(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            notificationService.createNotification(botContext.getUpdate().getMessage().getText(),
                    NotificationType.WHAT_CAN_BOT);
        }

        @Override
        public AdminBotState nextState() {
            return SelectTypeNotification;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    ChangeTextWhatIsBot(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext adminBotContext) {
            adminMessageService.sendChangeTextWhatIsBot(adminBotContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            String text = botContext.getUpdate().getMessage().getText();
            List<Notification> notification =
                    notificationService.getAllByType(NotificationType.WHAT_IS_BOT);
            if (!notification.isEmpty()) {
                Notification notificationForChange = notification.get(0);
                notificationForChange.setText(text);
                notificationService.save(notificationForChange);
                nextState = SuccessSaveText;
            } else {
                nextState = SelectTypeNotification;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    InputWhatIsBotText(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendInputWhatIsBotText(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            notificationService.createNotification(botContext.getUpdate().getMessage().getText(),
                    NotificationType.WHAT_IS_BOT);
        }

        @Override
        public AdminBotState nextState() {
            return SelectTypeNotification;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SettingWhatIsBotText(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendMenuSettingSelectedText(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            switch (botContext.getUpdate().getMessage().getText()) {
                case "Добавить":
                    List<Notification> notification =
                            notificationService.getAllByType(NotificationType.WHAT_IS_BOT);
                    if (notification.isEmpty()) {
                        nextState = InputWhatIsBotText;
                    } else {
                        nextState = FoundCurrentText;
                    }
                    break;
                case "Изменить":
                    nextState = ChangeTextWhatIsBot;
                    break;
                case "Назад":
                    nextState = SelectTypeNotification;
                    break;
                default:
                    nextState = MainMenu;
                    break;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    ChangeTextWebsite(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext adminBotContext) {
            adminMessageService.sendChangeTextWebsite(adminBotContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            String text = botContext.getUpdate().getMessage().getText();
            List<Notification> notification =
                    notificationService.getAllByType(NotificationType.WEBSITE);
            if (!notification.isEmpty()) {
                Notification notificationForChange = notification.get(0);
                notificationForChange.setText(text);
                notificationService.save(notificationForChange);
                nextState = SuccessSaveText;
            } else {
                nextState = SelectTypeNotification;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    InputWebsiteText(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendInputWebsiteText(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            notificationService.createNotification(botContext.getUpdate().getMessage().getText(),
                    NotificationType.WEBSITE);
        }

        @Override
        public AdminBotState nextState() {
            return SelectTypeNotification;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SettingWebsiteText(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendMenuSettingSelectedText(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            switch (botContext.getUpdate().getMessage().getText()) {
                case "Добавить":
                    List<Notification> notification =
                            notificationService.getAllByType(NotificationType.WEBSITE);
                    if (notification.isEmpty()) {
                        nextState = InputWebsiteText;
                    } else {
                        nextState = FoundCurrentText;
                    }
                    break;
                case "Изменить":
                    nextState = ChangeTextWebsite;
                    break;
                case "Назад":
                    nextState = SelectTypeNotification;
                    break;
                default:
                    nextState = MainMenu;
                    break;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    FoundCurrentText(false) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendMessageFoundText(botContext);
        }

        @Override
        public AdminBotState nextState() {
            return SelectTypeNotification;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    },

    SelectTypeNotification(true) {
        AdminBotState nextState;

        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendMenuSettingNotification(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            switch (botContext.getUpdate().getMessage().getText()) {
                case AdminReplyKeyboardMarkupSource.BEFORE_CONSULTATION:
                    nextState = SettingNotificationBeforeConsultation;
                    break;
                case AdminReplyKeyboardMarkupSource.AFTER_CONSULTATION:
                    nextState = SettingNotificationAfterConsultation;
                    break;
                case AdminReplyKeyboardMarkupSource.WHAT_IS_BOT:
                    nextState = SettingWhatIsBotText;
                    break;
                case AdminReplyKeyboardMarkupSource.WHAT_CAN_BOT:
                    nextState = SettingWhatCanBotText;
                    break;
                case AdminReplyKeyboardMarkupSource.WEBSITE:
                    nextState = SettingWebsiteText;
                    break;
                case AdminReplyKeyboardMarkupSource.BACK:
                    nextState = MainMenu;
                    break;
                default:
                    nextState = SelectTypeNotification;
                    break;
            }
        }

        @Override
        public AdminBotState nextState() {
            return nextState;
        }

        @Override
        public AdminBotState rootState() {
            return MainMenu;
        }
    };

    @Setter
    private static AdminMessageService adminMessageService;
    @Setter
    private static ClientService clientService;
    @Setter
    private static AdminService adminService;
    @Setter
    private static QuestionService questionService;
    @Setter
    private static MessageSender messageSender;
    @Setter
    private static MessengerService messengerService;
    @Setter
    private static PostponeMessageService postponeMessageService;
    @Setter
    private static NotificationService notificationService;

    private final Boolean isInputNeeded;

    AdminBotState(Boolean isInputNeeded) {
        this.isInputNeeded = isInputNeeded;
    }

    public Boolean getIsInputNeeded() {
        return isInputNeeded;
    }

    public static AdminBotState getInitialState() {
        return Start;
    }

    @Override
    public void handleText(AdminBotContext adminBotContext) throws AdminBotStateException {
    }

    @Override
    public void handleCallbackQuery(AdminBotContext adminBotContext) throws AdminBotStateException {
    }

    @Override
    public void handleContact(AdminBotContext adminBotContext) throws AdminBotStateException {
    }

    @Override
    public void handlePhoto(AdminBotContext adminBotContext) throws AdminBotStateException {
    }

    @Override
    public void handleVoice(AdminBotContext adminBotContext) throws AdminBotStateException {
    }

    @Override
    public void handleVideo(AdminBotContext adminBotContext) throws AdminBotStateException {
    }

    @Override
    public void handleVideoNote(AdminBotContext adminBotContext) throws AdminBotStateException {
    }

    @Override
    public void handleDocument(AdminBotContext adminBotContext) throws AdminBotStateException {
    }

    @Override
    public abstract void enter(AdminBotContext adminBotContext) throws AdminBotStateException;

    @Override
    public abstract AdminBotState nextState();

    @Override
    public abstract AdminBotState rootState();

}
