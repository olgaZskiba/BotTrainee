package by.integrator.telegrambot.bot.api.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import by.integrator.telegrambot.bot.api.UpdateHandler;
import by.integrator.telegrambot.bot.api.client.states.ClientBotContext;
import by.integrator.telegrambot.bot.api.client.states.ClientBotState;
import by.integrator.telegrambot.exception.ClientBotStateException;
import by.integrator.telegrambot.exception.UserNotFoundException;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.User;
import by.integrator.telegrambot.service.ClientService;
import by.integrator.telegrambot.service.UserService;

@Component
public class ClientUpdateHandler extends UpdateHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientUpdateHandler.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ClientService clientService;

    private void updateState(User user, ClientBotState clientBotState) {
        if (user != null && user.getClient() != null && clientBotState != null) {
            user.getClient().setClientBotState(clientBotState);
            userService.save(user);
        }
    }

    @Override
    public void processText(Update update) throws ClientBotStateException {
        final String chatId = update.getMessage().getChatId().toString();
        ClientBotContext botContext = null;
        ClientBotState botState = null;

        User user = userService.getByTelegramId(chatId);
        Client client = user.getClient();

        try {
            if (client == null) {
                client = clientService.createClient(user);

                botContext = ClientBotContext.of(client, update);
                botState = client.getClientBotState();

                botState.enter(botContext);

                while (!botState.getIsInputNeeded()) {
                    if (botState.nextState() != null) {
                        botState = botState.nextState();
                        botState.enter(botContext);
                        updateState(user, botState);
                    } else {
                        break;
                    }
                }
            } else {
                botContext = ClientBotContext.of(client, update);
                botState = client.getClientBotState();

                LOGGER.info("[" + chatId + " | " + botState + "] Text: " + update.getMessage().getText());

                botState.handleText(botContext);

                do {
                    if (botState.nextState() != null) {
                        botState = botState.nextState();
                        botState.enter(botContext);
                    } else {
                        break;
                    }
                } while (!botState.getIsInputNeeded());
            }
        } catch (ClientBotStateException ex) {
            botState = ((ClientBotStateException) ex).getExceptionState().rootState();
            botState.enter(botContext);
        } finally {
            updateState(user, botState);
        }
    }

    @Override
    public void processContact(Update update) throws ClientBotStateException {
        final String chatId = update.getMessage().getChatId().toString();
        ClientBotContext botContext = null;
        ClientBotState botState = null;

        User user = userService.getByTelegramId(chatId);
        Client client = user.getClient();

        try {
            if (client == null) {
                client = clientService.createClient(user);

                botContext = ClientBotContext.of(client, update);
                botState = client.getClientBotState();

                botState.enter(botContext);

                while (!botState.getIsInputNeeded()) {
                    if (botState.nextState() != null) {
                        botState = botState.nextState();
                        botState.enter(botContext);
                    } else {
                        break;
                    }
                }
            } else {
                botContext = ClientBotContext.of(client, update);
                botState = client.getClientBotState();

                LOGGER.info("[" + chatId + " | " + botState + "] Contact: " + update.getMessage().getContact().getPhoneNumber());

                botState.handleContact(botContext);

                do {
                    if (botState.nextState() != null) {
                        botState = botState.nextState();
                        botState.enter(botContext);
                    } else {
                        break;
                    }
                } while (!botState.getIsInputNeeded());
            }
        } catch (ClientBotStateException ex) {
            botState = ((ClientBotStateException) ex).getExceptionState().rootState();
            botState.enter(botContext);
        } finally {
            updateState(user, botState);
        }

    }

    @Override
    public void processPhoto(Update update) throws ClientBotStateException {
        final String chatId = update.getMessage().getChatId().toString();
        ClientBotContext botContext = null;
        ClientBotState botState = null;

        User user = userService.getByTelegramId(chatId);
        Client client = user.getClient();

        try {
            if (client == null) {
                client = clientService.createClient(user);

                botContext = ClientBotContext.of(client, update);
                botState = client.getClientBotState();

                botState.enter(botContext);

                while (!botState.getIsInputNeeded()) {
                    if (botState.nextState() != null) {
                        botState = botState.nextState();
                        botState.enter(botContext);
                    } else {
                        break;
                    }
                }
            } else {
                botContext = ClientBotContext.of(client, update);
                botState = client.getClientBotState();

                LOGGER.info("[" + chatId + " | " + botState + "] Photo: " + update.getMessage().getContact().getPhoneNumber());

                botState.handlePhoto(botContext);

                do {
                    if (botState.nextState() != null) {
                        botState = botState.nextState();
                        botState.enter(botContext);
                    } else {
                        break;
                    }
                } while (!botState.getIsInputNeeded());
            }
        } catch (ClientBotStateException ex) {
            botState = ((ClientBotStateException) ex).getExceptionState().rootState();
            botState.enter(botContext);
        } finally {
            updateState(user, botState);
        }
    }

    @Override
    public void processCallbackQuery(Update update) throws ClientBotStateException {
        final String chatId = update.getCallbackQuery().getFrom().getId().toString();
        ClientBotContext botContext = null;
        ClientBotState botState = null;

        User user = userService.getByTelegramId(chatId);
        Client client = user.getClient();

        try {
            if (client == null) {
                client = clientService.createClient(user);

                botContext = ClientBotContext.of(client, update);
                botState = client.getClientBotState();

                botState.enter(botContext);

                while (!botState.getIsInputNeeded()) {
                    if (botState.nextState() != null) {
                        botState = botState.nextState();
                        botState.enter(botContext);
                    } else {
                        break;
                    }
                }
            } else {
                botContext = ClientBotContext.of(client, update);
                botState = client.getClientBotState();

                LOGGER.info("[" + chatId + " | " + botState + "] CallbackQuery: " + update.getCallbackQuery().getData());

                botState.handleCallbackQuery(botContext);

                do {
                    if (botState.nextState() != null) {
                        botState = botState.nextState();
                        botState.enter(botContext);
                    } else {
                        break;
                    }
                } while (!botState.getIsInputNeeded());
            }
        } catch (ClientBotStateException ex) {
            botState = ((ClientBotStateException) ex).getExceptionState().rootState();
            botState.enter(botContext);
        } finally {
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
