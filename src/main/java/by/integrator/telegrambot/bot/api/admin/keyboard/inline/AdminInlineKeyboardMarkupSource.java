package by.integrator.telegrambot.bot.api.admin.keyboard.inline;

import by.integrator.telegrambot.bot.keyboard.InlineKeyboardMarkupSource;
import by.integrator.telegrambot.model.*;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public class AdminInlineKeyboardMarkupSource extends InlineKeyboardMarkupSource {

    public final static String FLAME = ":flame:";

    public InlineKeyboardMarkup generateClientsMultiplySelectablePageableInlineMarkup(List<Client> clients, Integer page) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        clients.sort(Comparator.comparing(Client::getProcessed));

        for (int i = ((page - 1) * ITEMS_PER_PAGE); i < page * ITEMS_PER_PAGE && i < clients.size(); i++) {
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();

            if (!clients.get(i).getProcessed()) {
                button.setText("🔥" + clients.get(i).getFirstName() + "🔥");
                button.setCallbackData(clients.get(i).getId().toString());
            } else {
                button.setText(clients.get(i).getFirstName());
                button.setCallbackData(clients.get(i).getId().toString());
            }

            buttons.add(button);

            keyboardRows.add(buttons);
        }

        if (clients.size() > ITEMS_PER_PAGE) {
            keyboardRows.add(getNavigateInlineButtons(clients, page));
        }

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getProcessedInlineButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();

        button.setText("Обработан");
        button.setCallbackData(CALLBACK_PREFIX + PROCESSED);

        buttons.add(button);

        keyboardRows.add(buttons);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getAnswerTheQuestionInlineButton(Client client, Question question) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();

        button.setText("Ответить");
        button.setCallbackData(CALLBACK_PREFIX + ANSWER + "." + client.getId() + "." + question.getId());

        buttons.add(button);

        keyboardRows.add(buttons);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getListButtonsPostponeMessage(PostponeMessage postponeMessage) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        InlineKeyboardButton buttonCancelLoadPicture = new InlineKeyboardButton();
        buttonCancelLoadPicture.setText("Удалить");
        buttonCancelLoadPicture.setCallbackData(postponeMessage.getId().toString());

        List<InlineKeyboardButton> firstKeyboardButtonRow = new ArrayList<>();
        firstKeyboardButtonRow.add(buttonCancelLoadPicture);
        keyboardRows.add(firstKeyboardButtonRow);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getDeleteNotificationButton(Notification notification) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        InlineKeyboardButton buttonCancelLoadPicture = new InlineKeyboardButton();
        buttonCancelLoadPicture.setText("Удалить");
        buttonCancelLoadPicture.setCallbackData(notification.getId().toString());

        List<InlineKeyboardButton> firstKeyboardButtonRow = new ArrayList<>();
        firstKeyboardButtonRow.add(buttonCancelLoadPicture);
        keyboardRows.add(firstKeyboardButtonRow);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getEditButtonsNotificationMessage(Notification notification) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        InlineKeyboardButton buttonCancelLoadPicture = new InlineKeyboardButton();
        buttonCancelLoadPicture.setText("Изменить");
        buttonCancelLoadPicture.setCallbackData(notification.getId().toString());

        List<InlineKeyboardButton> firstKeyboardButtonRow = new ArrayList<>();
        firstKeyboardButtonRow.add(buttonCancelLoadPicture);
        keyboardRows.add(firstKeyboardButtonRow);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getButtonsCancelLoadPicture() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonCancelLoadPicture = new InlineKeyboardButton();
        buttonCancelLoadPicture.setText("Без изображения");

        buttonCancelLoadPicture.setCallbackData("buttonCancelLoadPicture");

        List<InlineKeyboardButton> firstKeyboardButtonRow = new ArrayList<>();
        firstKeyboardButtonRow.add(buttonCancelLoadPicture);

        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        keyboardRows.add(firstKeyboardButtonRow);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }
}
