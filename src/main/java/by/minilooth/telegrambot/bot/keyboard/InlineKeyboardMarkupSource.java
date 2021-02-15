package by.minilooth.telegrambot.bot.keyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vdurmont.emoji.EmojiParser;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import by.minilooth.telegrambot.bot.api.BotState;
import by.minilooth.telegrambot.util.enums.Emoji;

public class InlineKeyboardMarkupSource {
    
    protected final static Integer ITEMS_PER_PAGE = 8;
    protected final static Integer START_PAGE = 1;
    protected final static String CALLBACK_PREFIX = "callback.";

    private final static String PREVIOUS_PAGE_TEXT = EmojiParser.parseToUnicode(Emoji.ARROW_LEFT.getAlias());
    private final static String NEXT_PAGE_TEXT = EmojiParser.parseToUnicode(Emoji.ARROW_RIGHT.getAlias()); 

    private final static String PREVIOUS_PAGE_CALLBACK_SUFFIX = "previous";
    private final static String NEXT_PAGE_CALLBACK_SUFFIX = "next";

    protected InlineKeyboardMarkup createInstance() {
        return new InlineKeyboardMarkup();
    } 

    protected <E extends Enum<E>, T> List<InlineKeyboardButton> getNavigateInlineButtons(Collection<?> items, Integer page, BotState<E, T> botState) {
        InlineKeyboardButton button = null;
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        if (page != START_PAGE) {
            button = new InlineKeyboardButton();
            button.setText(PREVIOUS_PAGE_TEXT);
            button.setCallbackData(generateCallbackData(botState, PREVIOUS_PAGE_CALLBACK_SUFFIX));
            buttons.add(button);
        }

        if (!page.equals(calculateCountOfPages(items))) {
            button = new InlineKeyboardButton();
            button.setText(NEXT_PAGE_TEXT);
            button.setCallbackData(generateCallbackData(botState, NEXT_PAGE_CALLBACK_SUFFIX));
            buttons.add(button);
        }

        return buttons;
    }

    protected Integer calculateCountOfPages(Collection<?> items) {
        return items.size() % ITEMS_PER_PAGE == 0 ? 
                    items.size() / ITEMS_PER_PAGE :
                    items.size() / ITEMS_PER_PAGE + 1;
    }

    protected <E extends Enum<E>, T> String generateCallbackData(BotState<E, T> botState, String suffix) {
        if (suffix == null || suffix.isEmpty() || suffix.isBlank()) {
            return CALLBACK_PREFIX + botState.toString().toLowerCase();
        }
        return CALLBACK_PREFIX + botState.toString().toLowerCase() + "." + suffix;
    }

}
