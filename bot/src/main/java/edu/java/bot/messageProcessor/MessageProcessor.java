package edu.java.bot.messageProcessor;

import edu.java.bot.command.Command;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageProcessor {

    List<? extends Command> commands();

    SendMessage process(Update update);

}
