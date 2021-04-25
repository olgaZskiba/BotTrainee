package by.integrator.telegrambot.bot.api.admin.states;

import by.integrator.telegrambot.bot.api.BotState;
import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.admin.keyboard.reply.AdminReplyKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.admin.service.AdminMessageService;
import by.integrator.telegrambot.bot.keyboard.InlineKeyboardMarkupSource;
import by.integrator.telegrambot.exception.AdminBotStateException;
import by.integrator.telegrambot.model.Admin;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.PostponeMessage;
import by.integrator.telegrambot.model.Question;
import by.integrator.telegrambot.service.*;
import by.integrator.telegrambot.validation.ValidationResult;
import by.integrator.telegrambot.validation.Validator;
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
    Start(true) {
        private AdminBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendStartMessage(botContext);
        }

        @SneakyThrows
        @Override
        public void handleText(AdminBotContext botContext) {
            String userAnswer = botContext.getUpdate().getMessage().getText();
            ValidationResult validationResult = Validator.isFirstNameValid(userAnswer);

            if (validationResult.getIsValid()) {
                Admin admin = botContext.getAdmin();
                admin.setFirstName(userAnswer);

                adminService.save(admin);
                nextState = MainMenu;
            } else {
                adminMessageService.sendInvalidInputMessage(botContext, validationResult.getMessage());
                nextState = null;
            }
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

    InputFirstName(true) {
        private AdminBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendInputFirstNameMessage(botContext);
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
            messageSender.sendMessage(botContext.getAdmin().getTelegramId(),
                    userAnswer, null);
        }

        @Override
        public void handleCallbackQuery(AdminBotContext adminBotContext) {
            String callbackData = adminBotContext.getUpdate().getCallbackQuery().getData();

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
//                        nextState = ClientProfile;
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
            Client client = botContext.getAdmin().getCurrentClient();
            client.setProcessed(true);
            clientService.save(client);
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
            System.out.println(callbackData);
            System.out.println(parseData);
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
            } else {
                if (userAnswer.startsWith(AdminReplyKeyboardMarkupSource.QUESTIONS)) {
                    nextState = ClientQuestions;
                    return;
                }
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
