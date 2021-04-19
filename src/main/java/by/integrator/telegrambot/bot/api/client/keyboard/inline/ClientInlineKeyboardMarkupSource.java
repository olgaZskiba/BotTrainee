package by.integrator.telegrambot.bot.api.client.keyboard.inline;

import by.integrator.telegrambot.bot.keyboard.InlineKeyboardMarkupSource;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.Messenger;
import by.integrator.telegrambot.model.enums.BotType;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ClientInlineKeyboardMarkupSource extends InlineKeyboardMarkupSource {

    public InlineKeyboardMarkup getListBotTypes(Collection<Messenger> selectedMessengers, List<Messenger> messenger) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        for (int i = 0; i < messenger.size(); i++) {
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();

            if (selectedMessengers.contains(messenger.get(i))) {
                button.setText(messenger.get(i).getName() + EmojiParser.parseToUnicode(" " + SELECTED_EMOJI));
                button.setCallbackData(messenger.get(i).getId().toString());
            } else {
                button.setText(messenger.get(i).getName());
                button.setCallbackData(messenger.get(i).getId().toString());
            }

            buttons.add(button);

            keyboardRows.add(buttons);
        }

        if (!selectedMessengers.isEmpty()) {
            keyboardRows.add(getContinueInlineButton());
        }

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getConfirmInlineKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton buttonYes = new InlineKeyboardButton();
        buttonYes.setText("Всё верно");
        buttonYes.setCallbackData("callback.yes");

        InlineKeyboardButton buttonNo = new InlineKeyboardButton();
        buttonNo.setText("Исправить");
        buttonNo.setCallbackData("callback.no");

        buttons.add(buttonYes);
        buttons.add(buttonNo);

        keyboardRows.add(buttons);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getClientFieldsInlineKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton buttonLastName = new InlineKeyboardButton();
        buttonLastName.setText("1 - Фамилия");
        buttonLastName.setCallbackData("callback.lastName");

        InlineKeyboardButton buttonFirstName = new InlineKeyboardButton();
        buttonFirstName.setText("2 - Имя");
        buttonFirstName.setCallbackData("callback.firstName");

        InlineKeyboardButton buttonEmail = new InlineKeyboardButton();
        buttonEmail.setText("3 - Email");
        buttonEmail.setCallbackData("callback.email");

        InlineKeyboardButton buttonMessenger = new InlineKeyboardButton();
        buttonMessenger.setText("4 - Мессенджер");
        buttonMessenger.setCallbackData("callback.messenger");

        buttons.add(buttonLastName);
        buttons.add(buttonFirstName);
        buttons.add(buttonEmail);
        buttons.add(buttonMessenger);

        keyboardRows.add(buttons);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }
}
