package by.integrator.telegrambot.bot.api.admin.keyboard.reply;

import by.integrator.telegrambot.bot.keyboard.ReplyKeyboardMarkupSource;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class AdminReplyKeyboardMarkupSource extends ReplyKeyboardMarkupSource {

    public static final String CLIENTS = "Клиенты";
    public static final String ORDERS = "Заказы";

    public static final String  YES = "Все верно";
    public static final String  NO = "Исправить";

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = this.createInstance(true, true, false);
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow firstKeyboardRowOne = new KeyboardRow();
        KeyboardRow firstKeyboardRowTwo = new KeyboardRow();
//        KeyboardRow firstKeyboardRowThree = new KeyboardRow();
//        KeyboardRow firstKeyboardRowFour = new KeyboardRow();
//        KeyboardRow firstKeyboardRowFive = new KeyboardRow();

        firstKeyboardRowOne.add(new KeyboardButton(CLIENTS));
        firstKeyboardRowOne.add(new KeyboardButton(ORDERS));


        keyboardRows.add(firstKeyboardRowOne);
//        keyboardRows.add(firstKeyboardRowTwo);
//        keyboardRows.add(firstKeyboardRowThree);
//        keyboardRows.add(firstKeyboardRowFour);
//        keyboardRows.add(firstKeyboardRowFive);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }
    
}
