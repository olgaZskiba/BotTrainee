package by.minilooth.telegrambot.util;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import by.minilooth.telegrambot.bot.TelegramBot;
import by.minilooth.telegrambot.bot.api.enums.UpdateType;

@Component
public class BotUtils {
    
    @Autowired
    private TelegramBot telegramBot;

    public org.telegram.telegrambots.meta.api.objects.File getTelegramFile(String fileId) throws TelegramApiException {
        return telegramBot.execute(GetFile.builder().fileId(fileId).build());
    }

    public java.io.File downloadTelegramFile(String fileId) throws TelegramApiException {
        return telegramBot.downloadFile(getTelegramFile(fileId));
    }

    public String getMaxResolutionPhotoFileId(List<PhotoSize> photos) {
        return Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null)).getFileId();
    }

    public UpdateType getUpdateType(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                return UpdateType.TEXT;
            }
            else if (update.getMessage().hasContact()) {
                return UpdateType.CONTACT;
            } 
            else if (update.getMessage().hasPhoto()) {
                return UpdateType.PHOTO;
            }
            else if (update.getMessage().hasVoice()) {
                return UpdateType.VOICE;
            }
            else if (update.getMessage().hasVideo()) {
                return UpdateType.VIDEO;
            }
            else if (update.getMessage().hasVideoNote()) {
                return UpdateType.VIDEO_NOTE;
            }
            else if (update.getMessage().hasDocument()) {
                return UpdateType.DOCUMENT;
            }
            else if (update.getMessage().hasAnimation()) {
                return UpdateType.ANIMATION;
            }
            else if (update.getMessage().hasSticker()) {
                return UpdateType.STICKER;
            }
            else if (update.getMessage().hasLocation()) {
                return UpdateType.LOCATION;
            }
        }
        else if (update.hasCallbackQuery()) {
            return UpdateType.CALLBACK_QUERY;
        }
        return null;
    }

}
