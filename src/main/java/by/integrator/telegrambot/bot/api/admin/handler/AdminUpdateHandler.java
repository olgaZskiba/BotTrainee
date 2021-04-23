package by.integrator.telegrambot.bot.api.admin.handler;

import by.integrator.telegrambot.bot.api.UpdateHandler;
import by.integrator.telegrambot.bot.api.admin.states.AdminBotContext;
import by.integrator.telegrambot.bot.api.admin.states.AdminBotState;
import by.integrator.telegrambot.exception.AdminBotStateException;
import by.integrator.telegrambot.exception.ClientBotStateException;
import by.integrator.telegrambot.model.Admin;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.User;
import by.integrator.telegrambot.service.AdminService;
import by.integrator.telegrambot.service.ClientService;
import by.integrator.telegrambot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AdminUpdateHandler extends UpdateHandler {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(AdminUpdateHandler.class);

    @Autowired private UserService userService;
    @Autowired private AdminService adminService;

    private void updateState(User user, AdminBotState adminBotState) {
        if (user != null && user.getClient() != null && adminBotState != null) {
            user.getAdmin().setAdminBotState(adminBotState);
            userService.save(user);
        }
    }

    @Override
    public void processText(Update update) throws AdminBotStateException {
        final String chatId = update.getMessage().getChatId().toString();
        AdminBotContext botContext = null;
        AdminBotState botState = null;

        User user = userService.getByTelegramId(chatId);
        Admin admin = user.getAdmin();

        try {
            if (admin == null) {
                admin = adminService.createAdmin(user);

                botContext = AdminBotContext.of(admin, update);
                botState = admin.getAdminBotState();

                botState.enter(botContext);
                
                while(!botState.getIsInputNeeded()) {
                    if (botState.nextState() != null) {
                        botState = botState.nextState();
                        botState.enter(botContext);
                    }
                    else {
                        break;
                    }
                }
            }
            else {
                botContext = AdminBotContext.of(admin, update);
                botState = admin.getAdminBotState();

                LOGGER.info("[{0} | {1}] Text: {2}", chatId, botState, update.getMessage().getText());

                botState.handleText(botContext);

                do {
                    if (botState.nextState() != null) {
                        botState = botState.nextState();
                        botState.enter(botContext);
                    }
                    else {
                        break;
                    }
                } while (!botState.getIsInputNeeded());
            }
        }
        catch (AdminBotStateException ex) {
            botState = ((AdminBotStateException) ex).getExceptionState().rootState();
            botState.enter(botContext);
        }
        finally {
            updateState(user, botState);
        }
    }

    @Override
    public void processContact(Update update) throws ClientBotStateException {

    }

    @Override
    public void processPhoto(Update update) {
        // TODO Auto-generated method stub

    }

    @Override
    public void processCallbackQuery(Update update) throws AdminBotStateException {
        final String chatId = update.getMessage().getChatId().toString();
        AdminBotContext botContext = null;
        AdminBotState botState = null;

        User user = userService.getByTelegramId(chatId);
        Admin admin = user.getAdmin();

        try {
            if (admin == null) {
                admin = adminService.createAdmin(user);

                botContext = AdminBotContext.of(admin, update);
                botState = admin.getAdminBotState();

                botState.enter(botContext);

                while(!botState.getIsInputNeeded()) {
                    if (botState.nextState() != null) {
                        botState = botState.nextState();
                        botState.enter(botContext);
                    }
                    else {
                        break;
                    }
                }
            }
            else {
                botContext = AdminBotContext.of(admin, update);
                botState = admin.getAdminBotState();

                LOGGER.info("[{0} | {1}] Text: {2}", chatId, botState, update.getMessage().getText());

                botState.handleCallbackQuery(botContext);

                do {
                    if (botState.nextState() != null) {
                        botState = botState.nextState();
                        botState.enter(botContext);
                    }
                    else {
                        break;
                    }
                } while (!botState.getIsInputNeeded());
            }
        }
        catch (AdminBotStateException ex) {
            botState = ((AdminBotStateException) ex).getExceptionState().rootState();
            botState.enter(botContext);
        }
        finally {
            updateState(user, botState);
        }

    }

    @Override
    public void processVoice(Update update) {
        // TODO Auto-generated method stub

    }

    @Override
    public void processVideo(Update update) {
        // TODO Auto-generated method stub

    }

    @Override
    public void processVideoNote(Update update) {
        // TODO Auto-generated method stub

    }

    @Override
    public void processDocument(Update update) {
        // TODO Auto-generated method stub

    }

    
}
