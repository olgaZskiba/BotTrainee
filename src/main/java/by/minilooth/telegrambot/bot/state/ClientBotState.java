package by.minilooth.telegrambot.bot.state;

import by.minilooth.telegrambot.bot.api.BotState;
import by.minilooth.telegrambot.bot.api.MessageSender;
import by.minilooth.telegrambot.bot.context.client.ClientBotContext;
import by.minilooth.telegrambot.bot.message.client.ClientMessageService;
import by.minilooth.telegrambot.exception.ClientBotStateException;
import by.minilooth.telegrambot.exception.ClientNotFoundException;
import lombok.Setter;
import lombok.SneakyThrows;

public enum ClientBotState implements BotState<ClientBotState, ClientBotContext> {
    Start(true) {
        @SneakyThrows
        @Override
        public void enter(ClientBotContext botContext) throws ClientBotStateException {
            clientMessageService.sendStartMessage(botContext);
        }

        @Override
        public void handleText(ClientBotContext botContext) {

        }

        @Override
        public ClientBotState nextState() {
            return MainMenu;
        }

        @Override
        public ClientBotState rootState() {
            return Start;
        }

    },

    MainMenu(true) {
        private ClientBotState nextState = null;

        @Override
        public void enter(ClientBotContext botContext) {

        }

        @Override
        public void handleText(ClientBotContext botContext) throws ClientBotStateException {
            throw new ClientBotStateException("adasd", this);
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

    EnterFirstname(true) {
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
    },

    EnteSurname(true) {
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

    @Setter private static ClientMessageService clientMessageService;

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
