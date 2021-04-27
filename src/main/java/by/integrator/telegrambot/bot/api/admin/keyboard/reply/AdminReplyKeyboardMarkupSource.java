package by.integrator.telegrambot.bot.api.admin.keyboard.reply;

import by.integrator.telegrambot.bot.keyboard.ReplyKeyboardMarkupSource;
import by.integrator.telegrambot.model.Question;
import by.integrator.telegrambot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class AdminReplyKeyboardMarkupSource extends ReplyKeyboardMarkupSource {

    public static final String CLIENTS = "Клиенты";
    public static final String POSTPONE = "Отложенные сообщения";
    public static final String QUESTIONS = "Активные вопросы";
    public static final String SETTING_TEXT = "Упраление текстами";
    public static final String BEFORE_CONSULTATION = "До консультации";
    public static final String AFTER_CONSULTATION = "После консультации";
    public static final String WHAT_CAN_BOT = "Что может бот";
    public static final String WHAT_IS_BOT = "Что такое бот";
    public static final String WEBSITE = "Сайт";
    public static final String BACK = "Назад";

    public static final String YES = "Все верно";
    public static final String NO = "Исправить";

    @Autowired
    private QuestionService questionService;

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = this.createInstance(true, true, false);

        List<Question> questions = questionService.getAllByIsSentAndIsAnswered(true, false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow firstKeyboardRowOne = new KeyboardRow();
        KeyboardRow firstKeyboardRowTwo = new KeyboardRow();
        KeyboardRow firstKeyboardRowThree = new KeyboardRow();
        KeyboardRow firstKeyboardRowFour = new KeyboardRow();


        firstKeyboardRowOne.add(new KeyboardButton(CLIENTS));
        firstKeyboardRowThree.add(new KeyboardButton(POSTPONE));
        firstKeyboardRowTwo.add(new KeyboardButton(QUESTIONS + " " + questions.size()));
        firstKeyboardRowFour.add(new KeyboardButton(SETTING_TEXT));

        keyboardRows.add(firstKeyboardRowTwo);
        keyboardRows.add(firstKeyboardRowThree);
        keyboardRows.add(firstKeyboardRowOne);
        keyboardRows.add(firstKeyboardRowFour);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboard getButtonsPostponeMessageMenu() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow KeyboardRowOne = new KeyboardRow();
        KeyboardRow KeyboardRowTwo = new KeyboardRow();
        KeyboardRow KeyboardRowThree = new KeyboardRow();

        KeyboardRowOne.add(new KeyboardButton("Добавление сообщений"));
        KeyboardRowTwo.add(new KeyboardButton("Удаление сообщений"));
        KeyboardRowThree.add(new KeyboardButton("Главное меню"));

        keyboardRows.add(KeyboardRowOne);
        keyboardRows.add(KeyboardRowTwo);
        keyboardRows.add(KeyboardRowThree);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboard getConfirmPostponeMessageKeyboard() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow KeyboardRowOne = new KeyboardRow();
        KeyboardRow KeyboardRowTwo = new KeyboardRow();
        KeyboardRow KeyboardRowThree = new KeyboardRow();

        KeyboardRowOne.add(new KeyboardButton("Да"));
        KeyboardRowTwo.add(new KeyboardButton("Нет"));
        KeyboardRowThree.add(new KeyboardButton("Назад"));

        keyboardRows.add(KeyboardRowOne);
        keyboardRows.add(KeyboardRowTwo);
        keyboardRows.add(KeyboardRowThree);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboard getMenuNotificationSetting() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow KeyboardRowOne = new KeyboardRow();
        KeyboardRow KeyboardRowTwo = new KeyboardRow();
        KeyboardRow KeyboardRowFour = new KeyboardRow();
        KeyboardRow KeyboardRowFive = new KeyboardRow();
        KeyboardRow KeyboardRowSix = new KeyboardRow();
        KeyboardRow KeyboardRowThree = new KeyboardRow();

        KeyboardRowOne.add(new KeyboardButton(BEFORE_CONSULTATION));
        KeyboardRowTwo.add(new KeyboardButton(AFTER_CONSULTATION));
        KeyboardRowFour.add(new KeyboardButton(WHAT_CAN_BOT));
        KeyboardRowFive.add(new KeyboardButton(WHAT_IS_BOT));
        KeyboardRowSix.add(new KeyboardButton(WEBSITE));
        KeyboardRowThree.add(new KeyboardButton(BACK));

        keyboardRows.add(KeyboardRowOne);
        keyboardRows.add(KeyboardRowTwo);
        keyboardRows.add(KeyboardRowFour);
        keyboardRows.add(KeyboardRowFive);
        keyboardRows.add(KeyboardRowSix);
        keyboardRows.add(KeyboardRowThree);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboard getMenuSettingSelectedNotification() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow KeyboardRowOne = new KeyboardRow();
        KeyboardRow KeyboardRowTwo = new KeyboardRow();
        KeyboardRow KeyboardRowThree = new KeyboardRow();

        KeyboardRowOne.add(new KeyboardButton("Добавить"));
        KeyboardRowTwo.add(new KeyboardButton("Удалить"));
        KeyboardRowTwo.add(new KeyboardButton("Изменить"));
        KeyboardRowThree.add(new KeyboardButton("Главное меню"));

        keyboardRows.add(KeyboardRowOne);
        keyboardRows.add(KeyboardRowTwo);
        keyboardRows.add(KeyboardRowThree);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboard getMenuSettingSelectedText() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow KeyboardRowOne = new KeyboardRow();
        KeyboardRow KeyboardRowTwo = new KeyboardRow();
        KeyboardRow KeyboardRowThree = new KeyboardRow();

        KeyboardRowOne.add(new KeyboardButton("Добавить"));
        KeyboardRowTwo.add(new KeyboardButton("Изменить"));
        KeyboardRowThree.add(new KeyboardButton("Главное меню"));

        keyboardRows.add(KeyboardRowOne);
        keyboardRows.add(KeyboardRowTwo);
        keyboardRows.add(KeyboardRowThree);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }

}
