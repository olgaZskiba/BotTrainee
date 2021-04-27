package by.integrator.telegrambot.bot.api.client.states;

import by.integrator.telegrambot.bot.api.BotState;
import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.admin.service.AdminMessageService;
import by.integrator.telegrambot.bot.api.client.keyboard.reply.ClientReplyKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.client.service.ClientMessageService;
import by.integrator.telegrambot.exception.ClientBotStateException;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.Messenger;
import by.integrator.telegrambot.model.Question;
import by.integrator.telegrambot.service.ClientService;
import by.integrator.telegrambot.service.MessengerService;
import by.integrator.telegrambot.service.QuestionService;
import by.integrator.telegrambot.validation.ValidationResult;
import by.integrator.telegrambot.validation.Validator;
import com.vdurmont.emoji.EmojiParser;
import lombok.Setter;
import lombok.SneakyThrows;

public enum ClientBotState implements BotState<ClientBotState, ClientBotContext> {
    Start(false) {
        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendStartMessage(botContext);
        }

        @Override
        public ClientBotState nextState() {
            return Greeting;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }

    },

    Greeting(false) {
        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendStartTwoMessage(botContext);
        }

        @Override
        public ClientBotState nextState() {
            return MainMenu;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }

    },

    MainMenu(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendMainMenuMessage(botContext);
        }

        @Override
        public void handleText(ClientBotContext botContext) throws ClientBotStateException {
            String userAnswer = botContext.getUpdate().getMessage().getText();
            switch (userAnswer) {
                case ClientReplyKeyboardMarkupSource
                        .WHAT_IS_THE_BOT:
                    nextState = WhatIsTheBot;
                    break;
                case ClientReplyKeyboardMarkupSource
                        .WHAT_CAN_BOT:
                    nextState = WhatCanBot;
                    break;
                case ClientReplyKeyboardMarkupSource
                        .ASK_QUESTION:
                    nextState = StartAskQuestion;
                    break;
                case ClientReplyKeyboardMarkupSource
                        .WEBSITE:
                    nextState = Website;
                    break;
                case ClientReplyKeyboardMarkupSource
                        .CREATE_TZ:
                    nextState = CreateTechnicalTask;
                    break;
                case ClientReplyKeyboardMarkupSource
                        .CONTINUE_COMMUNICATION:
                    nextState = InputFirstName;
                    break;
                default:
                    nextState = MainMenu;
                    break;
            }
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    WhatIsTheBot(false) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendWhatIsTheBotMessage(botContext);
        }

        @Override
        public ClientBotState nextState() {
            return MainMenu;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    CreateTechnicalTask(false) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendLinkToBrif(botContext);
//            adminMessageService.sendNotificationToAdminAboutNewTz(botContext);
        }

        @Override
        public ClientBotState nextState() {
            return MainMenu;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    InputFirstName(false) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendInputFirstNameMessage(botContext);
        }

        @Override
        public void handleText(ClientBotContext clientBotContext) {
            String userAnswer = clientBotContext.getUpdate().getMessage().getText();
            ValidationResult validationResult = Validator.isFirstNameValid(userAnswer);

            if (validationResult.getIsValid()) {
                Client client = clientBotContext.getClient();
                client.setFirstName(userAnswer);
                client.setFillStarted(true);
                clientService.save(client);
                nextState = WhatProblemsShouldBotSolve;
            } else {
                clientMessageService.sendInvalidInputMessage(clientBotContext, validationResult.getMessage());
                nextState = null;
            }
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    ChangeFirstName(false) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendInputFirstNameMessage(botContext);
        }

        @Override
        public void handleText(ClientBotContext clientBotContext) {
            String userAnswer = clientBotContext.getUpdate().getMessage().getText();
            ValidationResult validationResult = Validator.isFirstNameValid(userAnswer);

            if (validationResult.getIsValid()) {
                Client client = clientBotContext.getClient();
                client.setFirstName(userAnswer);

                clientService.save(client);
                nextState = ConfirmFilledProfile;
            } else {
                clientMessageService.sendInvalidInputMessage(clientBotContext, validationResult.getMessage());
                nextState = null;
            }
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    WhatProblemsShouldBotSolve(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendWhatProblemsShouldBotSolve(botContext);
        }

        @Override
        public void handleCallbackQuery(ClientBotContext clientBotContext) {
            String data = clientBotContext.getUpdate().getCallbackQuery().getData();
            Client client = clientBotContext.getClient();
            String currentText = client.getProblem();
            String tt = "";
            switch (data) {
                case "callback.reduce":
                    tt = clientService.checkProblem(currentText, "\n - Сократить денежные затраты на общение с клиентами;");
                    client.setProblem(tt);
                    clientService.save(client);
                    nextState = WhatProblemsShouldBotSolve;
                    break;
                case "callback.optimize":
                    tt = clientService.checkProblem(currentText, "\n - Оптимизировать работу сотрудников отдела продаж;");
                    client.setProblem(tt);
                    nextState = WhatProblemsShouldBotSolve;
                    break;
                case "callback.automate":
                    tt = clientService.checkProblem(currentText, "\n - Автоматизировать процесс продажи;");
                    client.setProblem(tt);
                    nextState = WhatProblemsShouldBotSolve;
                    break;
                case "callback.increase":
                    tt = clientService.checkProblem(currentText, "\n - Увеличить до ходимость клиентов до отдела продаж;");
                    client.setProblem(tt);
                    nextState = WhatProblemsShouldBotSolve;
                    break;
                case "callback.next":
                    nextState = InputGoals;
                    break;
                default:
                    nextState = WhatProblemsShouldBotSolve;
                    break;
            }
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    InputGoals(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendInputGoals(botContext);
        }

        @Override
        public void handleText(ClientBotContext clientBotContext) {
            Client client = clientBotContext.getClient();
            client.setGoals(clientBotContext.getUpdate().getMessage().getText());
            clientService.save(client);
        }

        @Override
        public ClientBotState nextState() {
            return InputFieldOfActivity;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    ChangeGoals(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendInputGoals(botContext);
        }

        @Override
        public void handleText(ClientBotContext clientBotContext) {
            Client client = clientBotContext.getClient();
            client.setGoals(clientBotContext.getUpdate().getMessage().getText());
            clientService.save(client);
        }

        @Override
        public ClientBotState nextState() {
            return ConfirmFilledProfile;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    InputFieldOfActivity(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendInputFieldOfActivity(botContext);
        }

        @Override
        public void handleText(ClientBotContext clientBotContext) {
            Client client = clientBotContext.getClient();
            client.setFieldOfActivity(clientBotContext.getUpdate().getMessage().getText());
            clientService.save(client);
        }

        @Override
        public ClientBotState nextState() {
            return SelectBotType;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    ChangeFieldOfActivity(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendInputFieldOfActivity(botContext);
        }

        @Override
        public void handleText(ClientBotContext clientBotContext) {
            Client client = clientBotContext.getClient();
            client.setFieldOfActivity(clientBotContext.getUpdate().getMessage().getText());
            clientService.save(client);
        }

        @Override
        public ClientBotState nextState() {
            return ConfirmFilledProfile;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    ChangeWhatProblemsShouldBotSolve(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendWhatProblemsShouldBotSolve(botContext);
        }

        @Override
        public void handleCallbackQuery(ClientBotContext clientBotContext) {
            String data = clientBotContext.getUpdate().getCallbackQuery().getData();
            Client client = clientBotContext.getClient();
            String currentText = client.getProblem();
            String tt = "";
            switch (data) {
                case "callback.reduce":
                    client.setProblem(clientService.checkProblem(currentText, "\n - Сократить денежные затраты на общение с клиентами;"));
                    clientService.save(client);
                    nextState = ChangeWhatProblemsShouldBotSolve;
                    break;
                case "callback.optimize":
                    client.setProblem(clientService.checkProblem(currentText, "\n - Оптимизировать работу сотрудников отдела продаж;"));
                    nextState = ChangeWhatProblemsShouldBotSolve;
                    break;
                case "callback.automate":
                    client.setProblem(clientService.checkProblem(currentText, "\n - Автоматизировать процесс продажи;"));
                    nextState = ChangeWhatProblemsShouldBotSolve;
                    break;
                case "callback.increase":
                    client.setProblem(clientService.checkProblem(currentText, "\n - Увеличить до ходимость клиентов до отдела продаж;"));
                    nextState = ChangeWhatProblemsShouldBotSolve;
                    break;
                case "callback.next":
                    nextState = ConfirmFilledProfile;
                    break;
                default:
                    nextState = ChangeWhatProblemsShouldBotSolve;
                    break;
            }
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    ChangeWayCommunication(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendWayCommunicationMessage(botContext);
        }

        @Override
        public void handleCallbackQuery(ClientBotContext clientBotContext) {
            String userAnswer = clientBotContext.getUpdate().getCallbackQuery().getData();
            Client client = clientBotContext.getClient();
            client.setWayCommunication(userAnswer);
            clientService.save(client);
        }

        @Override
        public ClientBotState nextState() {
            return ConfirmFilledProfile;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    WayCommunication(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendWayCommunicationMessage(botContext);
        }

        @Override
        public void handleCallbackQuery(ClientBotContext clientBotContext) {
            String userAnswer = clientBotContext.getUpdate().getCallbackQuery().getData();
            Client client = clientBotContext.getClient();
            client.setWayCommunication(userAnswer);
            clientService.save(client);
        }

        @Override
        public ClientBotState nextState() {
            return ConfirmFilledProfile;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    ChangeEmail(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendInputEmailMessage(botContext);
        }

        @Override
        public void handleText(ClientBotContext clientBotContext) {
            String userAnswer = clientBotContext.getUpdate().getMessage().getText();
            ValidationResult validationResult = Validator.isEmailValid(userAnswer);

            if (validationResult.getIsValid()) {
                Client client = clientBotContext.getClient();
                client.setEmail(userAnswer);

                clientService.save(client);
                nextState = ConfirmFilledProfile;
            } else {
                clientMessageService.sendInvalidInputMessage(clientBotContext, validationResult.getMessage());
                nextState = null;
            }
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    InputPhoneNumber(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendInputPhoneNumberMessage(botContext);
        }

        @Override
        public void handleContact(ClientBotContext clientBotContext) {
            String userAnswer = clientBotContext.getUpdate().getMessage().getContact().getPhoneNumber();
            Client client = clientBotContext.getClient();
            client.setPhoneNumber(userAnswer);

            clientService.save(client);
            nextState = WayCommunication;
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    ChangeSelectBotType(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendSelectBotTypeMessage(botContext);
        }

        @Override
        public void handleCallbackQuery(ClientBotContext clientBotContext) {
            String callbackData = clientBotContext.getUpdate().getCallbackQuery().getData();
            Client client = clientBotContext.getClient();

            if (callbackData.equals("callback.continue")) {
//                messageSender.deleteBotLastMessage(clientBotContext.getClient().getUser());
                nextState = ConfirmFilledProfile;
            } else {
                try {
                    Messenger messenger = messengerService.getById(Integer.parseInt(callbackData));
                    if (messenger != null) {
                        if (clientService.isHaveMessenger(client, messenger)) {
                            messengerService.removeMessengerFromClient(client, messenger);
                        } else {
                            messengerService.addMessengerToClient(client, messenger);
                        }
                    }
                } finally {
                    nextState = ChangeSelectBotType;
                }
            }
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    SelectBotType(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendSelectBotTypeMessage(botContext);
        }

        @Override
        public void handleCallbackQuery(ClientBotContext clientBotContext) {
            String callbackData = clientBotContext.getUpdate().getCallbackQuery().getData();
            Client client = clientBotContext.getClient();

            if (callbackData.equals("callback.continue")) {
                nextState = IntegrationToCrm;
            } else {
                try {
                    Messenger messenger = messengerService.getById(Integer.parseInt(callbackData));
                    if (messenger != null) {
                        if (clientService.isHaveMessenger(client, messenger)) {
                            messengerService.removeMessengerFromClient(client, messenger);
                        } else {
                            messengerService.addMessengerToClient(client, messenger);
                        }
                    }
                } finally {
                    nextState = SelectBotType;
                }
            }
        }

        @Override
        public void handleText(ClientBotContext clientBotContext) {
            String userAnswer = clientBotContext.getUpdate().getMessage().getText();
            Client client = clientBotContext.getClient();
            client.setUserMessenger(userAnswer);
            clientService.save(client);
            nextState = IntegrationToCrm;
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    IntegrationToCrm(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendSelectIntegrationToCrm(botContext);
        }

        @Override
        public void handleCallbackQuery(ClientBotContext clientBotContext) {
            String callbackData = clientBotContext.getUpdate().getCallbackQuery().getData();
            Client client = clientBotContext.getClient();
            switch (callbackData) {
                case "callback.yes":
                    client.setIntegrationToCrm("Да");
                    clientService.save(client);
                    nextState = ConfirmationForConsultation;
                    break;
                case "callback.no":
                    client.setIntegrationToCrm("Нет");
                    clientService.save(client);
                    nextState = ConfirmationForConsultation;
                    break;
                case "callback.dontKnow":
                    client.setIntegrationToCrm("Я не работал\\ла с CRM-системами");
                    clientService.save(client);
                    nextState = ConfirmationForConsultation;
                    break;
                default:
                    nextState = IntegrationToCrm;
                    break;
            }
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    ChangeIntegrationToCrm(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendSelectIntegrationToCrm(botContext);
        }

        @Override
        public void handleCallbackQuery(ClientBotContext clientBotContext) {
            String callbackData = clientBotContext.getUpdate().getCallbackQuery().getData();
            Client client = clientBotContext.getClient();
            switch (callbackData) {
                case "callback.yes":
                    client.setIntegrationToCrm("Да");
                    clientService.save(client);
                    nextState = ConfirmFilledProfile;
                    break;
                case "callback.not":
                    client.setIntegrationToCrm("Нет");
                    clientService.save(client);
                    nextState = ConfirmFilledProfile;
                    break;
                case "callback.dontKnow":
                    client.setIntegrationToCrm("Я не работал\\ла с CRM-системами");
                    clientService.save(client);
                    nextState = ConfirmFilledProfile;
                    break;
                default:
                    nextState = ChangeIntegrationToCrm;
                    break;
            }
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    ConfirmationForConsultation(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendConfirmationForConsultation(botContext);
        }

        @Override
        public void handleCallbackQuery(ClientBotContext clientBotContext) {
            String callbackData = clientBotContext.getUpdate().getCallbackQuery().getData();
            switch (callbackData) {
                case "callback.consultation":
                    nextState = InputPhoneNumber;
                    break;
                default:
                    nextState = ConfirmationForConsultation;
                    break;
            }
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    ConfirmFilledProfile(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendProfileMessage(botContext);
        }

        @Override
        public void handleCallbackQuery(ClientBotContext clientBotContext) {
            String callbackData = clientBotContext.getUpdate().getCallbackQuery().getData();

            switch (callbackData) {
                case "callback.yes":
                    nextState = FinishFilledProfile;
                    break;
                case "callback.no":
                    nextState = SelectFieldForChange;
                    break;
                default:
                    break;
            }
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    SelectFieldForChange(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendSelectFieldForChangeMessage(botContext);
        }

        @Override
        public void handleCallbackQuery(ClientBotContext clientBotContext) {
            String callbackData = clientBotContext.getUpdate().getCallbackQuery().getData();

            switch (callbackData) {
                case "callback.whatProblemsShouldBotSolve":
                    nextState = ChangeWhatProblemsShouldBotSolve;
                    break;
                case "callback.firstName":
                    nextState = ChangeFirstName;
                    break;
                case "callback.fieldOfActivity":
                    nextState = ChangeFieldOfActivity;
                    break;
                case "callback.messenger":
                    nextState = ChangeSelectBotType;
                    break;
                case "callback.wayCommunication":
                    nextState = ChangeWayCommunication;
                    break;
                case "callback.goals":
                    nextState = ChangeGoals;
                    break;
                default:
                    nextState = SelectFieldForChange;
                    break;
            }
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    FinishFilledProfile(false) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendFinishFilledProfileMessage(botContext);
            Client client = botContext.getClient();
            client.setProfileFilled(true);
            clientService.save(client);
            adminMessageService.sendNotificationAboutNewClient(botContext);
        }

        @Override
        public ClientBotState nextState() {
            return MainMenu;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    Website(false) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendWebsiteMessage(botContext);
        }

        @Override
        public ClientBotState nextState() {
            return MainMenu;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    StartAskQuestion(false) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendAskQuestionMessage(botContext);
        }

        @Override
        public ClientBotState nextState() {
            return AskQuestion;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    AskQuestion(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
        }

        @Override
        public void handleText(ClientBotContext botContext) {
            try {
                switch (EmojiParser.removeAllEmojis(botContext.getUpdate().getMessage().getText())) {
                    case "Отправить":
                        nextState = SaveSendingQuestion;
                        break;
                    case "Отмена":
                        nextState = CancelSendingQuestion;
                        break;
                    default:
                        String text = botContext.getUpdate().getMessage().getText();
                        Client client = botContext.getClient();
                        if (questionService.getByClientAndIsSent(client, false) == null) {
                            questionService.createQuestion(client, text);
                        } else {
                            questionService.saveQuestionText(botContext.getClient(), text);
                        }
                        nextState = AskQuestion;
                        break;
                }
            } catch (Exception ex) {
                clientMessageService.sendSomethingWentWrongMessage(botContext);
//                clientService.removeCurrents(botContext.getClient());
                nextState = AskQuestion;
            }
        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    CancelSendingQuestion(false) {
        @Override
        public void enter(ClientBotContext clientBotContext) {
            Question question = questionService.getByClientAndIsSent(clientBotContext.getClient(), false);
            if (question != null) {
                questionService.delete(question);
                clientMessageService.sendCancelSendingQuestionMessage(clientBotContext);
            } else {
                clientMessageService.sendSomethingWentWrongMessage(clientBotContext);
            }
        }

        @Override
        public ClientBotState nextState() {
            return MainMenu;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    SaveSendingQuestion(false) {
        @Override
        public void enter(ClientBotContext clientBotContext) {
            Question question = questionService.getByClientAndIsSent(clientBotContext.getClient(), false);
            if (question != null) {
                question.setIsSent(true);
                questionService.save(question);
                clientMessageService.sendSendingQuestionMessage(clientBotContext);
                adminMessageService.sendNotificationAboutNewClientQuestion(clientBotContext);
            } else {
                clientMessageService.sendSomethingWentWrongMessage(clientBotContext);
            }
        }

        @Override
        public ClientBotState nextState() {
            return MainMenu;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    WhatCanBot(false) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendWhatCanBotMessage(botContext);
        }

//        @Override
//        public void handleText(ClientBotContext botContext) {
//
//        }

        @Override
        public ClientBotState nextState() {
            return MainMenu;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    EnterPatronymic(true) {
        private ClientBotState nextState = null;

        @Override
        public void enter(ClientBotContext botContext) {

        }

        @Override
        public void handleText(ClientBotContext botContext) {

        }

        @Override
        public ClientBotState nextState() {
            return nextState;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    };

    @Setter
    private static ClientMessageService clientMessageService;
    @Setter
    private static AdminMessageService adminMessageService;
    @Setter
    private static ClientService clientService;
    @Setter
    private static MessageSender messageSender;
    @Setter
    private static MessengerService messengerService;
    @Setter
    private static QuestionService questionService;


    private final Boolean isInputNeeded;

    ClientBotState(Boolean isInputNeeded) {
        this.isInputNeeded = isInputNeeded;
    }

    public Boolean getIsInputNeeded() {
        return isInputNeeded;
    }

    public static ClientBotState getInitialState() {
        return Start;
    }

    @Override
    public void handleText(ClientBotContext clientBotContext) throws ClientBotStateException {
    }

    @Override
    public void handleCallbackQuery(ClientBotContext clientBotContext) throws ClientBotStateException {
    }

    @Override
    public void handleContact(ClientBotContext clientBotContext) throws ClientBotStateException {
    }

    @Override
    public void handlePhoto(ClientBotContext clientBotContext) throws ClientBotStateException {
    }

    @Override
    public void handleVoice(ClientBotContext clientBotContext) throws ClientBotStateException {
    }

    @Override
    public void handleVideo(ClientBotContext clientBotContext) throws ClientBotStateException {
    }

    @Override
    public void handleVideoNote(ClientBotContext clientBotContext) throws ClientBotStateException {
    }

    @Override
    public void handleDocument(ClientBotContext clientBotContext) throws ClientBotStateException {
    }

    @Override
    public abstract void enter(ClientBotContext clientBotContext) throws ClientBotStateException;

    @Override
    public abstract ClientBotState nextState();

    @Override
    public abstract ClientBotState rootState();

}
