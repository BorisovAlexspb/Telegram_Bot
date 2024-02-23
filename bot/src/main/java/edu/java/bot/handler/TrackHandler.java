package edu.java.bot.handler;

import edu.java.bot.Bot;
import edu.java.bot.command.ParsedCommand;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@SuppressWarnings("ImportOrder")
public class TrackHandler extends AbstractHandler {

    private static final String HELLO_LINE = "Hello, Please, write command and a link to the resource\n "
        + "Like: /untrack link";
    private static final String REMOVED_LINK_LINE = "Your link was removed\n";
    private static final String UNCORRECTED_LINK_LINE = "Your link is not correct\n";

    public TrackHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {

        if (parsedCommand.getText().isEmpty()) {
            bot.sendQueue.add(getTrackMessage(chatId, HELLO_LINE));
        } else {
            String possibleLink = parsedCommand.getText();
            if (isValidURL(possibleLink)) {
                // TODO: добавить ссылку в БД
                bot.sendQueue.add(getTrackMessage(chatId, REMOVED_LINK_LINE));
            } else {
                bot.sendQueue.add(getTrackMessage(chatId, UNCORRECTED_LINK_LINE));
            }
        }
        return "";
    }

    private SendMessage getTrackMessage(String chatID, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);
        sendMessage.setText(message);
        return sendMessage;
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
