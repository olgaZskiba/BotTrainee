package by.integrator.telegrambot.bot.api.admin.states;

import by.integrator.telegrambot.bot.api.BotState;
import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.admin.keyboard.reply.AdminReplyKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.admin.service.AdminMessageService;
import by.integrator.telegrambot.exception.AdminBotStateException;
import by.integrator.telegrambot.exception.ClientBotStateException;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.Messenger;
import by.integrator.telegrambot.service.ClientService;
import by.integrator.telegrambot.service.MessengerService;
import by.integrator.telegrambot.validation.ValidationResult;
import by.integrator.telegrambot.validation.Validator;
import lombok.Setter;
import lombok.SneakyThrows;

public enum AdminBotState implements BotState<AdminBotState, AdminBotContext> {
    Start(true) {
        private AdminBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendStartMessage(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {
            String userAnswer = botContext.getUpdate().getMessage().getText();
            switch (userAnswer) {
                case AdminReplyKeyboardMarkupSource.CLIENTS:
                    nextState = ClientList;
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

    ClientList(true) {
        private AdminBotState nextState = null;

        @SneakyThrows
        @Override
        public void enter(AdminBotContext botContext) {
            adminMessageService.sendClientListMessage(botContext);
        }

        @Override
        public void handleText(AdminBotContext botContext) {

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
            switch (userAnswer) {
                case AdminReplyKeyboardMarkupSource.CLIENTS:
                    nextState = ClientList;
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
    };

    @Setter
    private static AdminMessageService adminMessageService;

    @Setter
    private static ClientService clientService;

    @Setter
    private static MessageSender messageSender;
    @Setter
    private static MessengerService messengerService;


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
    public void handleText(AdminBotContext clientBotContext) throws AdminBotStateException {
    }

    @Override
    public void handleCallbackQuery(AdminBotContext clientBotContext) throws AdminBotStateException {
    }

    @Override
    public void handleContact(AdminBotContext clientBotContext) throws AdminBotStateException {
    }

    @Override
    public void handlePhoto(AdminBotContext clientBotContext) throws AdminBotStateException {
    }

    @Override
    public void handleVoice(AdminBotContext clientBotContext) throws AdminBotStateException {
    }

    @Override
    public void handleVideo(AdminBotContext clientBotContext) throws AdminBotStateException {
    }

    @Override
    public void handleVideoNote(AdminBotContext clientBotContext) throws AdminBotStateException {
    }

    @Override
    public void handleDocument(AdminBotContext clientBotContext) throws AdminBotStateException {
    }

    @Override
    public abstract void enter(AdminBotContext clientBotContext) throws AdminBotStateException;

    @Override
    public abstract AdminBotState nextState();

    @Override
    public abstract AdminBotState rootState();

}
