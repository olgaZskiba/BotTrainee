package by.integrator.telegrambot.exception;

import by.integrator.telegrambot.bot.api.client.states.ClientBotState;
import lombok.Getter;

public class ClientBotStateException extends Throwable {
    
    private static final long serialVersionUID = 5771105261629045380L;

    @Getter private final ClientBotState exceptionState;

    public ClientBotStateException(String message, ClientBotState clientBotState) {
        super(message);

        this.exceptionState = clientBotState;
    }

}
