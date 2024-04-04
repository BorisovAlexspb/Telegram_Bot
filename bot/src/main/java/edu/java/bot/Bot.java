package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.messageProcessor.MessageProcessor;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {
    private static final Logger LOG = Logger.getLogger(Bot.class.getName());

    private final MessageProcessor processor;

    public Bot(ApplicationConfig applicationConfig, MessageProcessor processor) {
        super(applicationConfig.telegramToken());
        this.processor = processor;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        } else {
            var sendMessage = processor.process(update);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        LOG.info("Receive new Update. updateID: " + update.getUpdateId());
    }

    @Override
    public String getBotUsername() {
        return "my bot";
    }

}
