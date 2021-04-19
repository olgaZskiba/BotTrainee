package by.integrator.telegrambot.model.enums;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public enum BotType {
    TELEGRAM("Telegram"),
    INSTAGRAM("Instagram"),
    VIBER("Viber"),
    FACEBOOK("Facebook");

    private String name;

    public String getName() {
        return name;
    }

    BotType(String name) {
        this.name = name;
    }
}
