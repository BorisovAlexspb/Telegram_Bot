package edu.java.bot.handler;

import edu.java.bot.Bot;
import edu.java.bot.command.ParsedCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ListHandler extends AbstractHandler {

    private static final String END_LINE = "\n";

    public ListHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        bot.sendQueue.add(getMessageList(chatId));
        return "";
    }

    private SendMessage getMessageList(String chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);

        StringBuilder text = new StringBuilder();
        text.append("List of your links:").append(END_LINE);

        sendMessage.setText(text.toString());
        return sendMessage;
    }
}
