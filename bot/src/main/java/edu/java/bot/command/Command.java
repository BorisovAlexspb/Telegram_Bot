package edu.java.bot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    String command();

    String description();

    SendMessage handle(Update update);

    default boolean supports(Update update) {
        var text = update.getMessage().getText();
        return text.split("\\s+", 2)[0].equals(command());
    }

}
