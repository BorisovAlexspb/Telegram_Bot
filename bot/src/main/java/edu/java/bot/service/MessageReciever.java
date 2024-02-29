package edu.java.bot.service;

import edu.java.bot.Bot;
import edu.java.bot.command.Command;
import edu.java.bot.command.ParsedCommand;
import edu.java.bot.command.Parser;
import edu.java.bot.handler.AbstractHandler;
import edu.java.bot.handler.DefaultHandler;
import edu.java.bot.handler.ListHandler;
import edu.java.bot.handler.SystemHandler;
import edu.java.bot.handler.TrackHandler;
import edu.java.bot.handler.UntrackHandler;
import java.util.logging.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@SuppressWarnings({"ReturnCount", "MultipleStringLiterals", "ImportOrder"})
public class MessageReciever implements Runnable {
    private static final Logger LOG = Logger.getLogger(MessageReciever.class.getName());
    private static final int WAIT_FOR_NEW_MESSAGE_DELAY = 1000;
    private Bot bot;
    private Parser parser;

    public MessageReciever(Bot bot) {
        this.bot = bot;
        parser = new Parser(bot.getBotName());
    }

    @Override
    public void run() {
        LOG.info("[STARTED] MsgReciever.  Bot class: " + bot);
        while (true) {
            for (Object object = bot.receiveQueue.poll(); object != null; object = bot.receiveQueue.poll()) {
                LOG.info("New object for analyze in queue " + object.toString());
                isUpdateMethod(object);
            }
            try {
                Thread.sleep(WAIT_FOR_NEW_MESSAGE_DELAY);
            } catch (InterruptedException e) {
                LOG.info("Catch interrupt. Exit" + e);
                return;
            }
        }
    }

    private void isUpdateMethod(Object object) {
        if (object instanceof Update) {
            Update update = (Update) object;
            LOG.info("Update recieved: " + update);
            analyzeForUpdateType(update);
        } else {
            LOG.info("Cant operate type of object: " + object.toString());
        }
    }

    private void analyzeForUpdateType(Update update) {
        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();

        ParsedCommand parsedCommand = parser.getParsedCommand(inputText);
        AbstractHandler handlerForCommand = getHandlerForCommand(parsedCommand.getCommand());

        String operationResult = handlerForCommand.operate(chatId.toString(), parsedCommand, update);

        if (!operationResult.isBlank()) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(operationResult);
            bot.sendQueue.add(message);
        }
    }

    private AbstractHandler getHandlerForCommand(Command command) {
        if (command == null) {
            LOG.info("Null command accepted. This is not good scenario.");
            return new DefaultHandler(bot);
        }
        switch (command) {
            case START:
            case HELP:
                SystemHandler systemHandler = new SystemHandler(bot);
                LOG.info("Handler for command[" + command.toString() + "] is: " + systemHandler);
                return systemHandler;
            case TRACK:
                TrackHandler trackHandler = new TrackHandler(bot);
                LOG.info("Handler for command[" + command.toString() + "] is: " + trackHandler);
                return trackHandler;
            case UNTRACK:
                UntrackHandler untrackHandler = new UntrackHandler(bot);
                LOG.info("Handler for command[" + command.toString() + "] is: " + untrackHandler);
                return untrackHandler;
            case LIST:
                ListHandler listHandler = new ListHandler(bot);
                LOG.info("Handler for command[" + command.toString() + "] is: " + listHandler);
                return listHandler;
            default:
                LOG.info("Handler for command[" + command.toString() + "] not Set. Return DefaultHandler");
                return new DefaultHandler(bot);
        }
    }
}
