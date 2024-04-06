package edu.java.bot.command;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.dto.LinkResponse;
import edu.java.bot.dto.ListLinkResponse;
import edu.java.bot.messageProcessor.MessageProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ListCommand extends BaseCommand {

    private static final String COMMAND = "/list";
    private static final String DESCRIPTION = "Показать список отслеживаемых ссылок.";
    private static final String EMPTY_LINK_LIST = "Вы не отслеживаете ни одной ссылки.";

    public ListCommand(MessageProcessor processor, ScrapperClient scrapperClient) {
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
        var id = update.getMessage().getChatId();
        ListLinkResponse listLinksResponse = scrapperClient.getLinks(id);
        if (listLinksResponse.size() == 0) {
            return new SendMessage(id.toString(), EMPTY_LINK_LIST);
        }
        var links = listLinksResponse.links();
        StringBuilder stringBuilder = new StringBuilder("Список отслеживаемых ссылок:\n");
        for (LinkResponse link : links) {
            stringBuilder.append(" - ").append(link.uri()).append("\n");
        }
        return new SendMessage(id.toString(), stringBuilder.toString());
    }
}
