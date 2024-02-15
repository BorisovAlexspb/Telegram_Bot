package edu.java.bot.handler;

import edu.java.bot.Bot;
import edu.java.bot.command.ParsedCommand;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@SuppressWarnings("ImportOrder")
public class UntrackHandler extends AbstractHandler {
    private static final String END_LINE = "\n";
    private static final Pattern URL_PATTERN =
        Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    public UntrackHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        if (parsedCommand.getText().isEmpty()) {
            bot.sendQueue.add(getUntrackMessage(chatId));
        } else {
            String possibleLink = parsedCommand.getText();
            if (checkURL(possibleLink)) {
                // TODO: удалить ссылку из БД
                bot.sendQueue.add(callbackCorrectLinkMessage(chatId));
            } else {
                bot.sendQueue.add(callBackWrongLinkMessage(chatId));
            }
        }
        return "";
    }

    private SendMessage getUntrackMessage(String chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);

        StringBuilder text = new StringBuilder();
        text.append("Hello. I'm  *").append(bot.getBotName()).append("*").append(END_LINE);
        text.append("Please, write command and a link to the resource").append(END_LINE);
        text.append("Like: /untrack link").append(END_LINE);

        sendMessage.setText(text.toString());
        return sendMessage;
    }

    private SendMessage callbackCorrectLinkMessage(String chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);

        StringBuilder text = new StringBuilder();
        text.append("Your link was removed").append(END_LINE);

        sendMessage.setText(text.toString());
        return sendMessage;
    }

    private SendMessage callBackWrongLinkMessage(String chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);

        StringBuilder text = new StringBuilder();
        text.append("Your link is not correct").append(END_LINE);

        sendMessage.setText(text.toString());
        return sendMessage;
    }

    private boolean checkURL(String path) {
        Matcher matcher = URL_PATTERN.matcher(path);
        return matcher.matches();
    }
}
