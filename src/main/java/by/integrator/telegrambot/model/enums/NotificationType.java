package by.integrator.telegrambot.model.enums;

import lombok.Getter;

public enum NotificationType {
    BEFORE("До консультации"),
    AFTER("После консультации");

    @Getter
    private String type;

    NotificationType(String type) {
        this.type = type;
    }
}
