package by.minilooth.telegrambot.bot.api;

import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class UpdateHandler implements UpdateProcessor {

    public void handle(Update update) throws Exception {
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                this.processInput(update);
            }
            else if (update.getMessage().hasContact()) {
                this.processContact(update);
            } 
            else if (update.getMessage().hasPhoto()) {
                this.processPhoto(update);
            }
            else if (update.getMessage().hasVoice()) {
                this.processPhoto(update);
            }
            else if (update.getMessage().hasVideo()) {
                this.processVideo(update);
            }
            else if (update.getMessage().hasVideoNote()) {
                this.processVideoNote(update);
            }
            else if (update.getMessage().hasDocument()) {
                this.processCallbackQuery(update);
            }
        }
        else if (update.hasCallbackQuery()) {
            this.processCallbackQuery(update);
        }
    }

}
