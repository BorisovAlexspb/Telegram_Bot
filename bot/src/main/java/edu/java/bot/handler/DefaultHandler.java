package edu.java.bot.handler;

import edu.java.bot.Bot;
import edu.java.bot.command.ParsedCommand;
import java.util.logging.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

@SuppressWarnings("ImportOrder")
public class DefaultHandler extends AbstractHandler {
    private static final Logger LOG = Logger.getLogger(DefaultHandler.class.getName());

    public DefaultHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        return "";
    }
}
