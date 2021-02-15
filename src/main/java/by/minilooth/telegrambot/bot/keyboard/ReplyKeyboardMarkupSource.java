package by.minilooth.telegrambot.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class ReplyKeyboardMarkupSource {

    protected ReplyKeyboardMarkup createInstance(Boolean selective, Boolean resizeKeyboard, Boolean oneTimeKeyboard) {
        return ReplyKeyboardMarkup.builder()
                                  .selective(selective)
                                  .resizeKeyboard(resizeKeyboard)
                                  .oneTimeKeyboard(oneTimeKeyboard)
                                  .build();
    }

}
