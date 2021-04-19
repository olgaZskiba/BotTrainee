package by.integrator.telegrambot.bot.api.client.states;

import by.integrator.telegrambot.bot.api.BotState;
import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.client.keyboard.reply.ClientReplyKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.client.service.ClientMessageService;
import by.integrator.telegrambot.exception.ClientBotStateException;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.Messenger;
import by.integrator.telegrambot.model.enums.BotType;
import by.integrator.telegrambot.service.ClientService;
import by.integrator.telegrambot.service.MessengerService;
import by.integrator.telegrambot.util.Utils;
import by.integrator.telegrambot.validation.ValidationResult;
import by.integrator.telegrambot.validation.Validator;
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
            return MainMenu;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }

    },

    AboutBot(false) {
        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendAboutBotMessage(botContext);
        }

        @Override
        public ClientBotState nextState() {
            return AnonsOne;
        }

        @Override
        public ClientBotState rootState() {
            return MainMenu;
        }
    },

    AnonsOne(false) {
        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendAnonsOneMessage(botContext);
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
                    nextState = AskQuestion;
                    break;
                case ClientReplyKeyboardMarkupSource
                        .WEBSITE:
                    nextState = Website;
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

    WhatIsTheBot(true) {
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

                clientService.save(client);
                nextState = InputLastName;
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

    InputLastName(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendInputLastNameMessage(botContext);
        }

        @Override
        public void handleText(ClientBotContext clientBotContext) {
            String userAnswer = clientBotContext.getUpdate().getMessage().getText();
            ValidationResult validationResult = Validator.isSurnameValid(userAnswer);

            if (validationResult.getIsValid()) {
                Client client = clientBotContext.getClient();
                client.setLastName(userAnswer);

                clientService.save(client);
                nextState = InputPhoneNumber;
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

    ChangeLastName(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendInputLastNameMessage(botContext);
        }

        @Override
        public void handleText(ClientBotContext clientBotContext) {
            String userAnswer = clientBotContext.getUpdate().getMessage().getText();
            ValidationResult validationResult = Validator.isSurnameValid(userAnswer);

            if (validationResult.getIsValid()) {
                Client client = clientBotContext.getClient();
                client.setLastName(userAnswer);

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

    InputEmail(true) {
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
                nextState = SelectBotType;
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
            nextState = InputEmail;
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
                    nextState = SelectBotType;
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
                    nextState = SelectBotType;
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
                case "callback.lastName":
                    nextState = ChangeLastName;
                    break;
                case "callback.firstName":
                    nextState = ChangeFirstName;
                    break;
                case "callback.email":
                    nextState = ChangeEmail;
                    break;
                case "callback.messenger":
                    nextState = ChangeSelectBotType;
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

    Website(true) {
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

    AskQuestion(true) {
        private ClientBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) {
            clientMessageService.sendAskQuestionMessage(botContext);
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

    WhatCanBot(true) {
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
    private static ClientService clientService;

    @Setter
    private static MessageSender messageSender;
    @Setter
    private static MessengerService messengerService;


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
