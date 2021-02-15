package by.minilooth.telegrambot.util;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import by.minilooth.telegrambot.bot.TelegramBot;

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

}
