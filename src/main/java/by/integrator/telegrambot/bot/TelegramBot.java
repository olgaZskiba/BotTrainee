package by.integrator.telegrambot.bot;

import by.integrator.telegrambot.bot.api.admin.handler.AdminUpdateHandler;
import by.integrator.telegrambot.bot.api.client.handler.ClientUpdateHandler;
import by.integrator.telegrambot.model.User;
import by.integrator.telegrambot.model.enums.Role;
import by.integrator.telegrambot.service.UserService;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Configuration
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LogManager.getLogger(TelegramBot.class);

    @Getter
    @Value("${telegram.botUsername}")
    private String botUsername;

    @Getter
    @Value("${telegram.botToken}")
    private String botToken;

    @Autowired
    private UserService userService;
    @Autowired
    private ClientUpdateHandler clientUpdateHandler;
    @Autowired
    private AdminUpdateHandler adminUpdateHandler;

    @Override
    public void onUpdateReceived(Update update) {
        final User user = userService.getByTelegramId(this.getTelegramId(update));

        if (user != null) {
            processUpdate(user, update);
        } else {
            register(update);
        }
    }

    private String getTelegramId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId().toString();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId().toString();
        }
        return null;
    }

    private void processUpdate(User user, Update update) {
        try {
            switch (user.getRole()) {
                case CLIENT:
                    clientUpdateHandler.handle(update);
                    break;
                case ADMIN:
                    adminUpdateHandler.handle(update);
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void register(Update update) {
        LOGGER.info("New user registered: " + this.getTelegramId(update));
        processUpdate(userService.createUser(update,
                Role.parseRole(update.getMessage().getText())),
                update);
    }
}
