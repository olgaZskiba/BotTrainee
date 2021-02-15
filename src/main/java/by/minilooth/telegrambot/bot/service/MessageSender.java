package by.minilooth.telegrambot.bot.service;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import by.minilooth.telegrambot.bot.TelegramBot;

@Component
public class MessageSender {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageSender.class); 
       
    private final static String DEFAULT_PARSE_MODE = "HTML";

    @Autowired private TelegramBot telegramBot;

    public Message sendMessage(String chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = SendMessage.builder()
                                             .chatId(chatId)
                                             .parseMode(DEFAULT_PARSE_MODE)
                                             .replyMarkup(replyKeyboard)
                                             .text(text)
                                             .build();

        return this.sendMessage(sendMessage);
    }

    public Message sendPhoto(String chatId, String caption, InputFile photo, ReplyKeyboard replyKeyboard) {
        SendPhoto sendPhoto = SendPhoto.builder()
                                       .chatId(chatId)
                                       .caption(caption)
                                       .photo(photo)
                                       .replyMarkup(replyKeyboard)
                                       .parseMode(DEFAULT_PARSE_MODE)
                                       .build();

        return this.sendPhoto(sendPhoto);
    }

    public Message sendDocument(String chatId, String caption, InputFile document, ReplyKeyboard replyKeyboard) {
        SendDocument sendDocument = SendDocument.builder()
                                                .chatId(chatId)
                                                .caption(caption)
                                                .document(document)
                                                .replyMarkup(replyKeyboard)
                                                .parseMode(DEFAULT_PARSE_MODE)
                                                .build();
                                        
        return this.sendDocument(sendDocument);
    }

    public Message sendVoice(String chatId, String caption, InputFile voice, ReplyKeyboard replyKeyboard) {
        SendVoice sendVoice = SendVoice.builder()
                                       .chatId(chatId)
                                       .caption(caption)
                                       .voice(voice)
                                       .replyMarkup(replyKeyboard)
                                       .parseMode(DEFAULT_PARSE_MODE)
                                       .build();

        return this.sendVoice(sendVoice);
    }

    public Message sendVideo(String chatId, String caption, InputFile video, ReplyKeyboard replyKeyboard) {
        SendVideo sendVideo = SendVideo.builder()
                                       .chatId(chatId)
                                       .caption(caption)
                                       .video(video)
                                       .replyMarkup(replyKeyboard)
                                       .parseMode(DEFAULT_PARSE_MODE)
                                       .build();

        return this.sendVideo(sendVideo);
    }

    public Message sendVideoNote(String chatId, InputFile videoNote, ReplyKeyboard replyKeyboard) {
        SendVideoNote sendVideoNote = SendVideoNote.builder()
                                                   .chatId(chatId)
                                                   .videoNote(videoNote)
                                                   .replyMarkup(replyKeyboard)
                                                   .build();

        return this.sendVideoNote(sendVideoNote);
    }


    public Message editMessageText(String chatId, String text, Integer messageId, InlineKeyboardMarkup InlineKeyboardMarkup) {
        EditMessageText editMessageText = EditMessageText.builder()
                                                         .chatId(chatId)
                                                         .text(text)
                                                         .messageId(messageId)
                                                         .replyMarkup(InlineKeyboardMarkup)
                                                         .parseMode(DEFAULT_PARSE_MODE)
                                                         .build();

        return this.editMessageText(editMessageText);
    }

    public Message editMessageReplyMarkup(String chatId, Integer messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageReplyMarkup editMessageReplyMarkup = EditMessageReplyMarkup.builder()
                                                                              .chatId(chatId)
                                                                              .messageId(messageId)
                                                                              .replyMarkup(inlineKeyboardMarkup)
                                                                              .build();

        return this.editMessageReplyMarkup(editMessageReplyMarkup);
    }

    public Boolean deleteMessage(String chatId, Integer messageId) {
        DeleteMessage deleteMessage = DeleteMessage.builder()
                                                   .chatId(chatId)
                                                   .messageId(messageId)
                                                   .build();

        return this.deleteMessage(deleteMessage);
    }

    private Message sendMessage(SendMessage sendMessage) {
        try {
            return telegramBot.execute(sendMessage);
        }
        catch (TelegramApiException ex) {
            LOGGER.error("Failed to send message to: " + sendMessage.getChatId() + ", reason: " + ex.getMessage());
            return null;
        }
    }

    private Message sendPhoto(SendPhoto sendPhoto) {
        try {
            return telegramBot.execute(sendPhoto);
        }
        catch (TelegramApiException ex) {
            LOGGER.error("Failed to send photo to: " + sendPhoto.getChatId() + ", reason: " + ex.getMessage());
            return null;
        }
    }

    private Message sendDocument(SendDocument sendDocument) {
        try {
            return telegramBot.execute(sendDocument);
        }
        catch (TelegramApiException ex) {
            LOGGER.error("Failed to send document to: " + sendDocument.getChatId() + ", reason: " + ex.getMessage());
            return null;
        }
    }

    private Message sendVoice(SendVoice sendVoice) {
        try {
            return telegramBot.execute(sendVoice);
        }
        catch (TelegramApiException ex) {
            LOGGER.error("Failed to send voice to: " + sendVoice.getChatId() + ", reason: " + ex.getMessage());
            return null;
        }
    }

    private Message sendVideo(SendVideo sendVideo) {
        try {
            return telegramBot.execute(sendVideo);
        }
        catch (TelegramApiException ex) {
            LOGGER.error("Failed to send video to: " + sendVideo.getChatId() + ", reason: " + ex.getMessage());
            return null;
        }
    }

    private Message sendVideoNote(SendVideoNote sendVideoNote) {
        try {
            return telegramBot.execute(sendVideoNote);
        }
        catch (TelegramApiException ex) {
            LOGGER.error("Failed to send video note to: " + sendVideoNote.getChatId() + ", reason: " + ex.getMessage());
            return null;
        }
    }

    private Message editMessageText(EditMessageText editMessageText) {
        try {
            Serializable result = telegramBot.execute(editMessageText);

            if (result instanceof Message) {
                return (Message) result;
            }
        }
        catch (TelegramApiException ex) {
            LOGGER.error("Failed to edit message text for: " + editMessageText.getChatId() + 
                    ", message id: " + editMessageText.getMessageId() + ", reason: " + ex.getMessage());
        }
        return null;
    }

    private Message editMessageReplyMarkup(EditMessageReplyMarkup editMessageReplyMarkup) {
        try {
            Serializable result = telegramBot.execute(editMessageReplyMarkup);

            if (result instanceof Message) {
                return (Message) result;
            }
        }
        catch (TelegramApiException ex) {
            LOGGER.error("Failed to edit message reply markup for: " + editMessageReplyMarkup.getChatId() + 
                    ", message id: " + editMessageReplyMarkup.getMessageId() + ", reason: " + ex.getMessage());
        }
        return null;
    }

    private Boolean deleteMessage(DeleteMessage deleteMessage) {
        try {
            return telegramBot.execute(deleteMessage);
        }
        catch (TelegramApiException ex) {
            LOGGER.error("Failed to delete message for: " + deleteMessage.getChatId() + 
                    ", message id: " + deleteMessage.getMessageId() + ", reason: " + ex.getMessage());
            return null;
        }
    }

}
