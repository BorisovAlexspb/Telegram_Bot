package edu.java.bot.command;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.messageProcessor.MessageProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand extends BaseCommand {

    private static final String COMMAND = "/start";
    private static final String DESCRIPTION = "Начать работу.";

    private static final String START_MESSAGE = "Бот запущен! Он поможет вам с отслеживанием ссылок.";
    private static final String REPEATED_REGISTRATION_MESSAGE = "Бот уже запущен.";

    public StartCommand(MessageProcessor processor, ScrapperClient scrapperClient) {
        super(processor, scrapperClient);
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String response = scrapperClient.registerChat(chatId);
        if (response == null) {
            return new SendMessage(chatId.toString(), REPEATED_REGISTRATION_MESSAGE);
        }
        return new SendMessage(chatId.toString(), START_MESSAGE);
    }
}
