package by.minilooth.telegrambot.bot.api;

public interface BotState<E extends Enum<E>, T> {
    
    public <X extends Throwable> void handleText(T botContext) throws X;
    public <X extends Throwable> void handleCallbackQuery(T botContext) throws X;
    public <X extends Throwable> void handleContact(T botContext) throws X;
    public <X extends Throwable> void handlePhoto(T botContext) throws X;
    public <X extends Throwable> void handleVoice(T botContext) throws X;
    public <X extends Throwable> void handleVideo(T botContext) throws X;
    public <X extends Throwable> void handleVideoNote(T botContext) throws X;
    public <X extends Throwable> void handleDocument(T botContext) throws X;

    public abstract <X extends Throwable> void enter(T botState) throws X;
    public abstract E nextState();
    public abstract E rootState();

}
