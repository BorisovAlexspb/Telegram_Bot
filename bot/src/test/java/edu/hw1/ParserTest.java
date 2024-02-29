package edu.hw1;

import edu.java.bot.command.Command;
import edu.java.bot.command.ParsedCommand;
import edu.java.bot.command.Parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserTest {
    private static final String botName = "bot";
    private Parser parser;

    @Before
    public void setParser() {
        parser = new Parser(botName);
    }

    @Test
    public void getParsedCommand_None() {
        String text = "just text";
        ParsedCommand parsedCommandAndText = parser.getParsedCommand(text);
        assertEquals(Command.NONE, parsedCommandAndText.getCommand());
        assertEquals(text, parsedCommandAndText.getText());
    }

    @Test
    public void getParsedCommand_NotForMe() {
        String text = "/test@another_Bot just text";
        ParsedCommand parsedCommandAndText = parser.getParsedCommand(text);
        assertEquals(Command.NOTFORME, parsedCommandAndText.getCommand());
    }

    @Test
    public void getParsedCommand_NoneButForMe() {
        String text = "/test@" + botName + " just text";
        ParsedCommand parsedCommandAndText = parser.getParsedCommand(text);
        assertEquals(Command.NONE, parsedCommandAndText.getCommand());
        assertEquals("just text", parsedCommandAndText.getText());
    }

}
