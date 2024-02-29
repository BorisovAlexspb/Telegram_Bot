package edu.java.bot.handler;

import edu.java.bot.Bot;
import edu.java.bot.command.ParsedCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractHandler {
    Bot bot;

    AbstractHandler(Bot bot) {
        this.bot = bot;
    }

    public abstract String operate(String chatId, ParsedCommand parsedCommand, Update update);
}
