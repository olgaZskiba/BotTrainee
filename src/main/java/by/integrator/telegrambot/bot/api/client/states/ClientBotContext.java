package by.integrator.telegrambot.bot.api.client.states;

import org.telegram.telegrambots.meta.api.objects.Update;

import by.integrator.telegrambot.model.Client;
import lombok.Getter;

@Getter
public class ClientBotContext {
    
    private final Client client;
    private final Update update;

    private ClientBotContext(Client client, Update update) {
        this.client = client;
        this.update = update;
    }

    public static ClientBotContext of(Client client, Update update) {
        return new ClientBotContext(client, update);
    }
    
}
