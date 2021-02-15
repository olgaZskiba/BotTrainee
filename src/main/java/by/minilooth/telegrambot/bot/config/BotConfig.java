package by.minilooth.telegrambot.bot.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import by.minilooth.telegrambot.bot.keyboard.InlineKeyboardMarkupSource;
import by.minilooth.telegrambot.bot.keyboard.ReplyKeyboardMarkupSource;
import by.minilooth.telegrambot.bot.keyboard.client.ClientReplyKeyboardMarkupSource;

@Component
public class BotConfig {

    private final static String MESSAGE_SOURCE_BASENAME = "classpath:messages";
    private final static String DEFAULT_ENCODING = "UTF-8";
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource =
            new ReloadableResourceBundleMessageSource();

        reloadableResourceBundleMessageSource.setBasename(MESSAGE_SOURCE_BASENAME);
        reloadableResourceBundleMessageSource.setDefaultEncoding(DEFAULT_ENCODING);

        return reloadableResourceBundleMessageSource;
    }

    @Bean
    public InlineKeyboardMarkupSource inlineKeyboardMarkupSource() {
        return new InlineKeyboardMarkupSource();
    }

    @Bean
    public ReplyKeyboardMarkupSource replyKeyboardMarkupSource() {
        return new ReplyKeyboardMarkupSource();
    }

    @Bean
    public ClientReplyKeyboardMarkupSource clientReplyKeyboardMarkupSource() {
        return new ClientReplyKeyboardMarkupSource();
    }

}
