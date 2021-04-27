package by.integrator.telegrambot.model.enums;

import lombok.Getter;

public enum NotificationType {
    BEFORE("До консультации"),
    AFTER("После консультации"),

    WHAT_CAN_BOT("Что может бот"),
    WHAT_IS_BOT("Что такое бот"),
    WEBSITE("Сайт");

    @Getter
    private String type;

    NotificationType(String type) {
        this.type = type;
    }
}
