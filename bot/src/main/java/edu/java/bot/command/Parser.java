package edu.java.bot.command;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings("ImportOrder")
public class Parser {
    private static final Logger LOG = Logger.getLogger(Parser.class.getName());
    private static final String PREFIX_FOR_COMMAND = "/";
    private static final String DELIMITER_COMMAND_BOTNAME = "@";
    private String botName;
    private static final Pattern URL_PATTERN =
        Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    public Parser(String botName) {
        this.botName = botName;
    }

    public ParsedCommand getParsedCommand(String text) {
        String trimText = "";
        if (text != null) {
            trimText = text.trim();
        }
        ParsedCommand result = new ParsedCommand(Command.NONE, trimText);

        if ("".equals(trimText)) {
            return result;
        }
        Pair<String, String> commandAndText = getDelimitedCommandFromText(trimText);
        if (isCommand(commandAndText.getKey())) {  // начинается с префикса /
            if (isCommandForMe(commandAndText.getKey())) {
                String commandForParse = cutCommandFromFullText(commandAndText.getKey());
                Command commandFromText = getCommandFromText(commandForParse);
                result.setText(commandAndText.getValue()); // текст который идёт за командой
                result.setCommand(commandFromText); // команда
            } else {
                result.setCommand(Command.NOTFORME);
                result.setText(commandAndText.getValue());
            }

        }
        return result;
    }

    private String cutCommandFromFullText(String text) {
        return text.contains(DELIMITER_COMMAND_BOTNAME)
            ? text.substring(1, text.indexOf(DELIMITER_COMMAND_BOTNAME))
            : text.substring(1);
    }

    private Command getCommandFromText(String text) {
        String upperCaseText = text.toUpperCase().trim();
        Command command = Command.NONE;
        try {
            command = Command.valueOf(upperCaseText);
        } catch (IllegalArgumentException e) {
            LOG.info("Can't parse command: " + text);
        }
        return command;
    }

    private Pair<String, String> getDelimitedCommandFromText(String trimText) {
        Pair<String, String> commandText;

        if (trimText.contains(" ")) {
            int indexOfSpace = trimText.indexOf(" ");
            commandText = new MutablePair<>(trimText.substring(0, indexOfSpace), trimText.substring(indexOfSpace + 1));
        } else {
            commandText = new MutablePair<>(trimText, "");
        }
        return commandText;
    }

    private boolean isCommandForMe(String command) {
        if (command.contains(DELIMITER_COMMAND_BOTNAME)) {
            String botNameForEqual = command.substring(command.indexOf(DELIMITER_COMMAND_BOTNAME) + 1);
            return botName.equals(botNameForEqual);
        }
        return true;
    }

    private boolean isCommand(String text) {
        return text.startsWith(PREFIX_FOR_COMMAND);
    }

    private boolean checkURL(String path) {
        Matcher matcher = URL_PATTERN.matcher(path);
        return matcher.matches();
    }
}
