package by.integrator.telegrambot.bot.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import by.integrator.telegrambot.util.BotUtils;

public abstract class UpdateHandler implements UpdateProcessor {

    @Autowired
    private BotUtils botUtils;

    public void handle(Update update) {
        switch(botUtils.getUpdateType(update)) {
            case CALLBACK_QUERY:
                this.processCallbackQuery(update);
                break;
            case TEXT:
                this.processText(update);
                break;
            case CONTACT:
                this.processContact(update);
                break;
            case PHOTO:
                this.processPhoto(update);
                break;
            case VOICE:
                this.processVoice(update);
                break;
            case VIDEO:
                this.processVideo(update);
                break;
            case VIDEO_NOTE:
                this.processVideoNote(update);
                break;
            case DOCUMENT:
                this.processDocument(update);
                break;
            default:
                break;
        }
    }

}
