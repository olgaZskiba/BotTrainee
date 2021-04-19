package by.integrator.telegrambot.bot.api;

public interface BotStateInjector<E extends Enum<E>, T> {

    public void inject();
    
}
