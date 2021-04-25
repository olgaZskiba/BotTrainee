package by.integrator.telegrambot.bot.api.admin.states;

import by.integrator.telegrambot.model.Admin;
import by.integrator.telegrambot.model.Client;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
public class AdminBotContext {
    
    private final Admin admin;
    private final Update update;

    private AdminBotContext(Admin admin, Update update) {
        this.admin = admin;
        this.update = update;
    }

    public static AdminBotContext of(Admin admin, Update update) {
        return new AdminBotContext(admin, update);
    }
    
}
