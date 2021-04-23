package by.integrator.telegrambot.bot.api;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProcessor {
    
    public <E extends Throwable> void processText(Update update) throws E;
    public <E extends Throwable> void processContact(Update update) throws E;
    public <E extends Throwable> void processPhoto(Update update) throws E;
    public <E extends Throwable> void processCallbackQuery(Update update) throws E;
    public <E extends Throwable> void processVoice(Update update) throws E;
    public <E extends Throwable> void processVideo(Update update) throws E;
    public <E extends Throwable> void processVideoNote(Update update) throws E;
    public <E extends Throwable> void processDocument(Update update) throws E;
    
}
