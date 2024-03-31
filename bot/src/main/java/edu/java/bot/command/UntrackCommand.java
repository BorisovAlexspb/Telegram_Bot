package edu.java.bot.command;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.dto.RemoveLinkRequest;
import edu.java.bot.messageProcessor.MessageProcessor;
import java.net.URI;
import java.net.URISyntaxException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UntrackCommand extends BaseCommand {

    private static final String COMMAND = "/untrack";
    private static final String DESCRIPTION = "Прекратить отслеживание ссылки.";

    private static final String UNTRACKED_LINK = "Вы и так не отслеживаете данную ссылку.";

    private static final String SUCCESS_UNTRACKING = "Отслеживание ссылки прекращено.";

    public UntrackCommand(MessageProcessor processor, ScrapperClient scrapperClient) {
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
        var message = update.getMessage();
        var split = message.getText().split("\\s+", 2);
        var chatId = message.getChatId();
        if (split.length == 1) {
            return new SendMessage(chatId.toString(), NOT_FOUND_LINK);
        } else {
            try {
                URI uri = new URI(split[1]);
                var linkResponse = scrapperClient.removeLink(chatId, new RemoveLinkRequest(uri.toString()));

                return new SendMessage(
                    chatId.toString(),
                    linkResponse.id() == null ? UNTRACKED_LINK : SUCCESS_UNTRACKING
                );
            } catch (URISyntaxException e) {
                return new SendMessage(chatId.toString(), UNSUPPORTED_LINK);
            }
        }
    }
}
