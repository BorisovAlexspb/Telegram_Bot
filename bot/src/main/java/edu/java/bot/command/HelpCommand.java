package edu.java.bot.command;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.messageProcessor.MessageProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelpCommand extends BaseCommand {
    private static final String COMMAND = "/help";
    private static final String DESCRIPTION = "Показать список доступных команд";

    public HelpCommand(MessageProcessor processor, ScrapperClient scrapperClient) {
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
        StringBuilder stringBuilder = new StringBuilder("Список команд:\n");
        for (Command command : processor.commands()) {
            stringBuilder.append("- ").append(command).append("\n");
        }
        return new SendMessage(update.getMessage().getChatId().toString(), stringBuilder.toString());
    }
}
