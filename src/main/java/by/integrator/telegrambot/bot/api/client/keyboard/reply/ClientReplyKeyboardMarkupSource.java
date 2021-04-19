package by.integrator.telegrambot.bot.api.client.keyboard.reply;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import by.integrator.telegrambot.bot.keyboard.ReplyKeyboardMarkupSource;

public class ClientReplyKeyboardMarkupSource extends ReplyKeyboardMarkupSource {

    public static final String WHAT_IS_THE_BOT = "Что такое бот?";
    public static final String WHAT_CAN_BOT = "Что я умею";
    public static final String ASK_QUESTION = "Задать свой вопрос";
    public static final String WEBSITE = "Сайт";
    public static final String CONTINUE_COMMUNICATION = "Продолжить общение";
    public static final String  SHARE_CONTACT = "Поделиться контактом";
    public static final String  YES = "Все верно";
    public static final String  NO = "Исправить";

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = this.createInstance(true, true, false);
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow firstKeyboardRowOne = new KeyboardRow();
        KeyboardRow firstKeyboardRowTwo = new KeyboardRow();
        KeyboardRow firstKeyboardRowThree = new KeyboardRow();
        KeyboardRow firstKeyboardRowFour = new KeyboardRow();
        KeyboardRow firstKeyboardRowFive = new KeyboardRow();

        firstKeyboardRowOne.add(new KeyboardButton(WHAT_IS_THE_BOT));
        firstKeyboardRowTwo.add(new KeyboardButton(WHAT_CAN_BOT));
        firstKeyboardRowThree.add(new KeyboardButton(ASK_QUESTION));
        firstKeyboardRowFour.add(new KeyboardButton(WEBSITE));
        firstKeyboardRowFive.add(new KeyboardButton(CONTINUE_COMMUNICATION));

        keyboardRows.add(firstKeyboardRowOne);
        keyboardRows.add(firstKeyboardRowTwo);
        keyboardRows.add(firstKeyboardRowThree);
        keyboardRows.add(firstKeyboardRowFour);
        keyboardRows.add(firstKeyboardRowFive);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getPhoneNumberKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = this.createInstance(true, true, false);
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow firstKeyboardRowOne = new KeyboardRow();

        KeyboardButton sharePhoneNumber = new KeyboardButton(SHARE_CONTACT);
        sharePhoneNumber.setRequestContact(true);

        firstKeyboardRowOne.add(sharePhoneNumber);

        keyboardRows.add(firstKeyboardRowOne);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }
    
}
