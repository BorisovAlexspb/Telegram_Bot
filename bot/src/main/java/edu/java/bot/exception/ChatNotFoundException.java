package edu.java.bot.exception;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(long id) {
        super("Chat not found " + id);
    }
}
