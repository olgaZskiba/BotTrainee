package by.minilooth.telegrambot.bot;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import by.minilooth.telegrambot.bot.handler.client.ClientUpdateHandler;
import by.minilooth.telegrambot.model.User;
import by.minilooth.telegrambot.model.enums.Role;
import by.minilooth.telegrambot.service.UserService;
import lombok.Getter;

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

    @Autowired private UserService userService;
    @Autowired private ClientUpdateHandler clientUpdateHandler;

    @Override
    public void onUpdateReceived(Update update) {
        final Optional<User> user = userService.getByTelegramId(this.getTelegramId(update));

        if (user.isPresent()) {
            processUpdate(user.get(), update);
        }
        else {
            register(update);
        }
    }

    private String getTelegramId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId().toString();
        }
        else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId().toString();
        }
        return null;
    }

    private void processUpdate(User user, Update update) {
        try {
            switch(user.getRole()) {
                case CLIENT:
                    clientUpdateHandler.handle(update);
                    break;
                default:
                    break;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void register(Update update) {
        LOGGER.info("New user registered: " + this.getTelegramId(update));
        processUpdate(userService.createUser(update, Role.parseRole(update.getMessage().getText())), update);
    }
}
