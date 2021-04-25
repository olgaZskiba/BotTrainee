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


        firstKeyboardRowOne.add(new KeyboardButton(CLIENTS));
        firstKeyboardRowThree.add(new KeyboardButton(POSTPONE));

        firstKeyboardRowTwo.add(new KeyboardButton(QUESTIONS + " " + questions.size()));

        keyboardRows.add(firstKeyboardRowTwo);
        keyboardRows.add(firstKeyboardRowThree);
        keyboardRows.add(firstKeyboardRowOne);

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

}
