package by.minilooth.telegrambot.bot.handler.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import by.minilooth.telegrambot.bot.api.UpdateHandler;
import by.minilooth.telegrambot.bot.context.client.ClientBotContext;
import by.minilooth.telegrambot.bot.state.ClientBotState;
import by.minilooth.telegrambot.exception.ClientBotStateException;
import by.minilooth.telegrambot.exception.UserNotFoundException;
import by.minilooth.telegrambot.model.Client;
import by.minilooth.telegrambot.model.User;
import by.minilooth.telegrambot.service.ClientService;
import by.minilooth.telegrambot.service.UserService;

@Component
public class ClientUpdateHandler extends UpdateHandler {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientUpdateHandler.class);

    @Autowired private UserService userService;
    @Autowired private ClientService clientService;

    private void updateState(User user, ClientBotState clientBotState) {
        if (user != null && user.getClient() != null && clientBotState != null) {
            user.getClient().setClientBotState(clientBotState);
            userService.save(user);
        }
    }

    @Override
    public void processInput(Update update) throws ClientBotStateException, UserNotFoundException {
        final Long chatId = update.getMessage().getChatId();
        ClientBotContext botContext = null;
        ClientBotState botState = null;

        User user = userService.getByTelegramId(chatId).orElseThrow(UserNotFoundException::new);
        Client client = user.getClient();

        try {
            if (client == null) {
                client = clientService.createClient(user);

                botContext = ClientBotContext.of(client, update);
                botState = client.getClientBotState();

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
                botContext = ClientBotContext.of(client, update);
                botState = client.getClientBotState();

                LOGGER.info("Update received from client: " + chatId + ", in state: " + botState + ", with text: " + update.getMessage().getText());

                botState.handleInput(botContext);

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
        catch (ClientBotStateException ex) {
            botState = ((ClientBotStateException) ex).getExceptionState().rootState();
            botState.enter(botContext);
        }
        finally {
            updateState(user, botState);
        }
    }

    @Override
    public void processContact(Update update) {
        // TODO Auto-generated method stub

    }

    @Override
    public void processPhoto(Update update) {
        // TODO Auto-generated method stub

    }

    @Override
    public void processCallbackQuery(Update update) {
        // TODO Auto-generated method stub

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
