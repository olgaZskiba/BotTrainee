package by.integrator.telegrambot.exception;

import by.integrator.telegrambot.bot.api.admin.states.AdminBotState;
import by.integrator.telegrambot.bot.api.client.states.ClientBotState;
import lombok.Getter;

public class AdminBotStateException extends Throwable {

    private static final long serialVersionUID = 5771105261629045380L;

    @Getter private final AdminBotState exceptionState;

    public AdminBotStateException(String message, AdminBotState adminBotState) {
        super(message);

        this.exceptionState = adminBotState;
    }

}
