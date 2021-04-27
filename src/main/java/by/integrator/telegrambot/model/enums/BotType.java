package by.integrator.telegrambot.model.enums;

public enum BotType {
    TELEGRAM("Telegram"),
    INSTAGRAM("Instagram"),
    VIBER("Viber"),
    WHATSAPP("WhatsApp"),
    FACEBOOK("Facebook");

    private String name;

    public String getName() {
        return name;
    }

    BotType(String name) {
        this.name = name;
    }
}
