package by.integrator.telegrambot.bot.api.admin.service;

import by.integrator.telegrambot.bot.api.MessageSender;
import by.integrator.telegrambot.bot.api.admin.keyboard.inline.AdminInlineKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.admin.keyboard.reply.AdminReplyKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.admin.states.AdminBotContext;
import by.integrator.telegrambot.bot.api.client.states.ClientBotContext;
import by.integrator.telegrambot.bot.api.enums.UpdateType;
import by.integrator.telegrambot.bot.message.MessageService;
import by.integrator.telegrambot.exception.ClientNotFoundException;
import by.integrator.telegrambot.model.Admin;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.Messenger;
import by.integrator.telegrambot.service.AdminService;
import by.integrator.telegrambot.service.ClientService;
import by.integrator.telegrambot.service.MessengerService;
import by.integrator.telegrambot.util.BotUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class AdminMessageService extends MessageService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AdminMessageService.class);

    @Autowired
    private BotUtils botUtils;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private AdminMessageSource adminMessageSource;
    @Autowired
    private AdminReplyKeyboardMarkupSource adminReplyKeyboardMarkupSource;
    @Autowired
    private AdminInlineKeyboardMarkupSource adminInlineKeyboardMarkupSource;
    @Autowired
    private MessengerService messengerService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ClientService clientService;

    @SneakyThrows
    private Boolean checkCallbackQuery(AdminBotContext clientBotContext) {
        return botUtils.getUpdateType(clientBotContext.getUpdate()).equals(UpdateType.CALLBACK_QUERY) &&
                !clientBotContext.getUpdate().getMessage().getReplyMarkup().equals(new InlineKeyboardMarkup());

    }

    @SneakyThrows
    private Boolean checkEditableMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();
        return admin.getUser().hasLastBotMessage() && admin.getUser().getBotLastMessageEditable() &&
                !messageSender.isMessageExpired(admin.getUser().getBotLastMessageDate());
    }

    public void sendStartMessage(AdminBotContext adminBotContext) throws ClientNotFoundException {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(admin.getTelegramId(), adminMessageSource.getMessage("message.start"),
                        adminReplyKeyboardMarkupSource.getMainMenuKeyboard());

                updateLastBotMessage(admin.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
            }
        }
    }

    public void sendMainMenuMessage(AdminBotContext adminBotContext) throws ClientNotFoundException {
        Admin admin = adminBotContext.getAdmin();

        if (admin == null) throw new ClientNotFoundException();

        if (checkEditableMessage(adminBotContext)) {

        } else {
            try {
                Message message = messageSender.sendMessage(admin.getTelegramId(), adminMessageSource.getMessage("message.mainMenu"),
                        adminReplyKeyboardMarkupSource.getMainMenuKeyboard());

                updateLastBotMessage(admin.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to send start message to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
            }
        }
    }

    @SneakyThrows
    public void sendNotificationAboutNewClient(ClientBotContext botContext) {
        List<Admin> admins = adminService.getAll();
        if (!admins.isEmpty()) {
            Client client = botContext.getClient();
            for (Admin admin : admins) {
                messageSender.sendMessage(admin.getTelegramId(),
                        adminMessageSource.getMessage("message.notificationAboutNewClient", client.getLastName(), client.getFirstName(),
                                client.getEmail(), client.getPhoneNumber(), messengerService.getClientMessengerList(client)), null);
            }
        }
    }

    @SneakyThrows
    public void sendClientListMessage(AdminBotContext adminBotContext) {
        Admin admin = adminBotContext.getAdmin();

        List<Client> clients = clientService.getAll();

        if (admin == null) throw new ClientNotFoundException();

        if (!clients.isEmpty()) {
            String list = "";
            for (int i = 0; i <= clients.size(); i++) {
                list = list.concat((i + 1) + ". ");
                list = list.concat(clients.get(i).getLastName() + " ");
                list = list.concat(clients.get(i).getFirstName() + " \n");
            }
            try {
                Message message = messageSender.sendMessage(admin.getTelegramId(), list,
                        adminReplyKeyboardMarkupSource.getMainMenuKeyboard());

                updateLastBotMessage(admin.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to sendClientListMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
            }
        } else {
            try {
                Message message = messageSender.sendMessage(admin.getTelegramId(), adminMessageSource.getMessage("message.clientListIsEmpty"),
                        adminReplyKeyboardMarkupSource.getMainMenuKeyboard());

                updateLastBotMessage(admin.getUser(), message);
            } catch (TelegramApiException ex) {
                LOGGER.error("Unable to sendClientListMessage to user: " + admin.getTelegramId() + ", reason: " + ex.getLocalizedMessage());
            }
        }
    }
}
