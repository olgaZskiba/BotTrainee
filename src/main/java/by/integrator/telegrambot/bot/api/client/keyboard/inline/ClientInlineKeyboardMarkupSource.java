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

    public InlineKeyboardMarkup getIntegrationToCrmKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton buttonYes = new InlineKeyboardButton();
        buttonYes.setText("Да");
        buttonYes.setCallbackData("callback.yes");

        List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        InlineKeyboardButton buttonNo = new InlineKeyboardButton();
        buttonNo.setText("Нет");
        buttonNo.setCallbackData("callback.no");

        List<InlineKeyboardButton> buttons3 = new ArrayList<>();
        InlineKeyboardButton buttonDontKnow = new InlineKeyboardButton();
        buttonDontKnow.setText("Я не работал\\ла с CRM-системами");
        buttonDontKnow.setCallbackData("callback.dontKnow");

        buttons.add(buttonYes);
        buttons2.add(buttonNo);
        buttons3.add(buttonDontKnow);

        keyboardRows.add(buttons);
        keyboardRows.add(buttons2);
        keyboardRows.add(buttons3);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getFreeConsultationKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton buttonFreeConsultation = new InlineKeyboardButton();
        buttonFreeConsultation.setText("Бесплатная консультация");
        buttonFreeConsultation.setCallbackData("callback.consultation");

        buttons.add(buttonFreeConsultation);

        keyboardRows.add(buttons);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getSkipKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton buttonSkip = new InlineKeyboardButton();
        buttonSkip.setText("Пропустить");
        buttonSkip.setCallbackData("callback.skip");

        buttons.add(buttonSkip);

        keyboardRows.add(buttons);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }


    public InlineKeyboardMarkup getWayCommunicationKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton buttonViber = new InlineKeyboardButton();
        buttonViber.setText("Viber");
        buttonViber.setCallbackData("Viber");

        List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        InlineKeyboardButton buttonWhatsapp = new InlineKeyboardButton();
        buttonWhatsapp.setText("Whatsapp");
        buttonWhatsapp.setCallbackData("Whatsapp");

        List<InlineKeyboardButton> buttons3 = new ArrayList<>();
        InlineKeyboardButton buttonTelegram = new InlineKeyboardButton();
        buttonTelegram.setText("Telegram");
        buttonTelegram.setCallbackData("Telegram");

        List<InlineKeyboardButton> buttons4 = new ArrayList<>();
        InlineKeyboardButton buttonMobile = new InlineKeyboardButton();
        buttonMobile.setText("Мобильный звонок");
        buttonMobile.setCallbackData("Мобильный звонок");

        buttons.add(buttonViber);
        buttons2.add(buttonWhatsapp);
        buttons3.add(buttonTelegram);
        buttons4.add(buttonMobile);

        keyboardRows.add(buttons);
        keyboardRows.add(buttons2);
        keyboardRows.add(buttons3);
        keyboardRows.add(buttons4);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getButtonsWhatProblemsShouldBotSolve(Client client) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton buttonReduce = new InlineKeyboardButton();
        buttonReduce.setText("Сократить денежные затраты на общение с клиентами");
        buttonReduce.setCallbackData("callback.reduce");

        List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        InlineKeyboardButton buttonOptimize = new InlineKeyboardButton();
        buttonOptimize.setText("Оптимизировать работу сотрудников отдела продаж");
        buttonOptimize.setCallbackData("callback.optimize");

        List<InlineKeyboardButton> buttons3 = new ArrayList<>();
        InlineKeyboardButton buttonAutomate = new InlineKeyboardButton();
        buttonAutomate.setText("Автоматизировать процесс продажи");
        buttonAutomate.setCallbackData("callback.automate");

        List<InlineKeyboardButton> buttons4 = new ArrayList<>();
        InlineKeyboardButton buttonIncrease = new InlineKeyboardButton();
        buttonIncrease.setText("Увеличить до ходимость клиентов до отдела продаж");
        buttonIncrease.setCallbackData("callback.increase");

        if(client.getProblem() != null) {
            String[] parse = client.getProblem().split(" - ");
            if (parse.length != 0) {
                for (String current : parse) {
                    if (current.startsWith("Сократить")) {
                        buttonReduce.setText(EmojiParser.parseToUnicode(" " + SELECTED_EMOJI) + "Сократить денежные затраты на общение с клиентами");
                    } else if (current.startsWith("Оптимизировать")) {
                        buttonOptimize.setText(EmojiParser.parseToUnicode(" " + SELECTED_EMOJI) + "Оптимизировать работу сотрудников отдела продаж");
                    } else if (current.startsWith("Автоматизировать")) {
                        buttonAutomate.setText(EmojiParser.parseToUnicode(" " + SELECTED_EMOJI) + "Автоматизировать процесс продажи");
                    } else if (current.startsWith("Увеличить")) {
                        buttonIncrease.setText(EmojiParser.parseToUnicode(" " + SELECTED_EMOJI) + "Увеличить до ходимость клиентов до отдела продаж");
                    }
                }
            }
        }

        List<InlineKeyboardButton> buttons5 = new ArrayList<>();
        InlineKeyboardButton buttonNext = new InlineKeyboardButton();
        buttonNext.setText("Продолжить");
        buttonNext.setCallbackData("callback.next");

        buttons.add(buttonReduce);
        buttons2.add(buttonOptimize);
        buttons3.add(buttonAutomate);
        buttons4.add(buttonIncrease);
        buttons5.add(buttonNext);

        keyboardRows.add(buttons);
        keyboardRows.add(buttons2);
        keyboardRows.add(buttons3);
        keyboardRows.add(buttons4);
        keyboardRows.add(buttons5);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getClientFieldsInlineKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton whatProblemsShouldBotSolve = new InlineKeyboardButton();
        whatProblemsShouldBotSolve.setText("1 - Какие проблемы должен решать бот, и какие бизнес-процессы должны быть автоматизированы с его помощью?");
        whatProblemsShouldBotSolve.setCallbackData("callback.whatProblemsShouldBotSolve");

        List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        InlineKeyboardButton buttonFirstName = new InlineKeyboardButton();
        buttonFirstName.setText("2 - Как к вам обращаться?");
        buttonFirstName.setCallbackData("callback.firstName");

        List<InlineKeyboardButton> buttons3 = new ArrayList<>();
        InlineKeyboardButton buttonGoals = new InlineKeyboardButton();
        buttonGoals.setText("3 - Для каких целей Вам нужен чат-бот (ассистент) ?");
        buttonGoals.setCallbackData("callback.goals");

        List<InlineKeyboardButton> buttons4 = new ArrayList<>();
        InlineKeyboardButton buttonFieldOfActivity = new InlineKeyboardButton();
        buttonFieldOfActivity.setText("4 - Какая сфера деятельности Вашей компании?");
        buttonFieldOfActivity.setCallbackData("callback.fieldOfActivity");

        List<InlineKeyboardButton> buttons5 = new ArrayList<>();
        InlineKeyboardButton buttonWayCommunication = new InlineKeyboardButton();
        buttonWayCommunication.setText("6 - Способ связи");
        buttonWayCommunication.setCallbackData("callback.wayCommunication");

        List<InlineKeyboardButton> buttons6 = new ArrayList<>();
        InlineKeyboardButton buttonMessenger = new InlineKeyboardButton();
        buttonMessenger.setText("5 - Для каких платформ будет разрабатываться чат-бот?");
        buttonMessenger.setCallbackData("callback.messenger");

        buttons.add(buttonFirstName);
        buttons2.add(whatProblemsShouldBotSolve);
        buttons3.add(buttonGoals);
        buttons4.add(buttonFieldOfActivity);
        buttons5.add(buttonMessenger);
        buttons6.add(buttonWayCommunication);

        keyboardRows.add(buttons);
        keyboardRows.add(buttons2);
        keyboardRows.add(buttons3);
        keyboardRows.add(buttons4);
        keyboardRows.add(buttons5);
        keyboardRows.add(buttons6);

        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }
}
