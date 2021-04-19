package by.integrator.telegrambot.util.enums;

public enum Emoji {
    ARROW_RIGHT(":arrow_right:"),
    ARROW_LEFT(":arrow_left:"),
    ARROW_DOWN(":arrow_down:"),
    WHITE_CHECK_MARK(":white_check_mark:");

    private String alias;

    Emoji(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }
}
