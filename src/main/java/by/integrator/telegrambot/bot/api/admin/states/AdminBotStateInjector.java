package by.integrator.telegrambot.bot.api.admin.states;

import by.integrator.telegrambot.bot.api.BotStateInjector;
import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.admin.service.AdminMessageService;
import by.integrator.telegrambot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AdminBotStateInjector implements BotStateInjector<AdminBotState, AdminBotContext> {

    @Autowired private AdminMessageService adminMessageService;
    @Autowired private ClientService clientService;
    @Autowired private MessageSender messageSender;
    @Autowired private MessengerService messengerService;
    @Autowired private AdminService adminService;
    @Autowired private QuestionService questionService;
    @Autowired private PostponeMessageService postponeMessageService;

    @PostConstruct
    @Override
    public void inject() {
        AdminBotState.setAdminMessageService(adminMessageService);
        AdminBotState.setClientService(clientService);
        AdminBotState.setMessageSender(messageSender);
        AdminBotState.setMessengerService(messengerService);
        AdminBotState.setAdminService(adminService);
        AdminBotState.setQuestionService(questionService);
        AdminBotState.setPostponeMessageService(postponeMessageService);
    }

}
