package edu.java.bot.command;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.messageProcessor.MessageProcessor;

public abstract class BaseCommand implements Command {
    protected static final String UNSUPPORTED_LINK = """
        Неверный формат ссылки или ресурс.
        Доступные для отслеживания ресурсы: Github, StackOverflow

        Формат ссылок:
        Github: https://www.github.com/{owner}/{repository}
        StackOverflow: https://www.stackoverflow.com/questions/{question_id}
       """;

    protected static final String NOT_FOUND_LINK = "Укажите ссылку.";

    protected final MessageProcessor processor;
    protected final ScrapperClient scrapperClient;

    public BaseCommand(MessageProcessor processor, ScrapperClient scrapperClient) {
        this.processor = processor;
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String toString() {
        return command() + ": " + description();
    }
}
