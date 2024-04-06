package edu.java.bot.command;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.messageProcessor.MessageProcessor;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TrackCommand extends BaseCommand {

    private static final String COMMAND = "/track";
    private static final String DESCRIPTION = "Начать отслеживание ссылки.";

    private static final String SUCCESS_TRACKING = "Отслеживание ссылки начато успешно.";
    private static final String ALREADY_TACKED = "Вы уже отслеживаете данную ссылку.";

    public TrackCommand(MessageProcessor processor, ScrapperClient scrapperClient) {
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
                if (!isValidURL(uri.toString())) {
                    return new SendMessage(chatId.toString(), UNSUPPORTED_LINK);
                }
                var linkResponse = scrapperClient.addLink(chatId, new AddLinkRequest(uri.toString()));

                return new SendMessage(
                    chatId.toString(),
                    linkResponse.id() == null ? ALREADY_TACKED : SUCCESS_TRACKING
                );
            } catch (URISyntaxException e) {
                return new SendMessage(chatId.toString(), UNSUPPORTED_LINK);
            }
        }
    }

    private boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
