package by.integrator.telegrambot.bot.config;

import by.integrator.telegrambot.bot.api.admin.keyboard.inline.AdminInlineKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.admin.keyboard.reply.AdminReplyKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.client.keyboard.inline.ClientInlineKeyboardMarkupSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import by.integrator.telegrambot.bot.keyboard.InlineKeyboardMarkupSource;
import by.integrator.telegrambot.bot.keyboard.ReplyKeyboardMarkupSource;
import by.integrator.telegrambot.bot.api.client.keyboard.reply.ClientReplyKeyboardMarkupSource;

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

    @Bean
    public ClientInlineKeyboardMarkupSource clientInlineKeyboardMarkupSource() {
        return new ClientInlineKeyboardMarkupSource();
    }

    @Bean
    public AdminReplyKeyboardMarkupSource adminReplyKeyboardMarkupSource() {
        return new AdminReplyKeyboardMarkupSource();
    }

    @Bean
    public AdminInlineKeyboardMarkupSource adminInlineKeyboardMarkupSource() {
        return new AdminInlineKeyboardMarkupSource();
    }

}
