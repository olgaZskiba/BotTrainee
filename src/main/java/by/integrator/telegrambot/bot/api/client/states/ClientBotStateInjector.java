package by.integrator.telegrambot.bot.api.client.states;

import javax.annotation.PostConstruct;

import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.admin.service.AdminMessageService;
import by.integrator.telegrambot.bot.api.client.service.ClientMessageService;
import by.integrator.telegrambot.service.ClientService;
import by.integrator.telegrambot.service.MessengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import by.integrator.telegrambot.bot.api.BotStateInjector;

@Component
public class ClientBotStateInjector implements BotStateInjector<ClientBotState, ClientBotContext> {

    @Autowired private ClientMessageService clientMessageService;
    @Autowired private ClientService clientService;
    @Autowired private MessageSender messageSender;
    @Autowired private MessengerService messengerService;
    @Autowired private AdminMessageService adminMessageService;

    @PostConstruct
    @Override
    public void inject() {
        ClientBotState.setClientMessageService(clientMessageService);
        ClientBotState.setClientService(clientService);
        ClientBotState.setMessageSender(messageSender);
        ClientBotState.setMessengerService(messengerService);
        ClientBotState.setAdminMessageService(adminMessageService);
    }

}
