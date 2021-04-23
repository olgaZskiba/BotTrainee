package by.integrator.telegrambot.service.bot;

import by.integrator.telegrambot.model.Messenger;
import by.integrator.telegrambot.model.enums.BotType;
import by.integrator.telegrambot.service.MessengerService;
import by.integrator.telegrambot.service.async.ProfileMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitializationService {

    @Autowired
    private MessengerService messengerService;
    @Autowired
    private ProfileMessageSender profileMessageSender;

    public void initialize() {
        if (messengerService.getAll().isEmpty()) {
            for (BotType type: BotType.values()) {
                Messenger messenger = Messenger.builder()
                        .name(type.getName())
                        .build();
                messengerService.save(messenger);
            }
        }
        profileMessageSender.runAsync();
    }

}
