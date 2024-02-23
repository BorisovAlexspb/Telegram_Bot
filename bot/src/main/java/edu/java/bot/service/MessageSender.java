package edu.java.bot.service;

import edu.java.bot.Bot;
import java.util.logging.Logger;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@SuppressWarnings("MultipleStringLiterals")
public class MessageSender implements Runnable {
    private static final Logger LOG = Logger.getLogger(MessageSender.class.getName());
    private static final int SENDER_SLEEP_TIME = 1000;
    private Bot bot;

    public MessageSender(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        LOG.info("[STARTED] MsgSender.  Bot class: " + bot);
        try {
            while (true) {
                for (Object object = bot.sendQueue.poll(); object != null; object = bot.sendQueue.poll()) {
                    LOG.info("Get new msg to send " + object);
                    send(object);
                }
                try {
                    Thread.sleep(SENDER_SLEEP_TIME);
                } catch (InterruptedException e) {
                    LOG.info("Take interrupt while operate msg list " + e);
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    private void send(Object object) {
        try {
            String messageType = isBotApiMethod(object);
            switch (messageType) {
                case "EXECUTE":
                    BotApiMethod<Message> message = (BotApiMethod<Message>) object;
                    LOG.info("Use Execute for " + object);
                    bot.execute(message);
                    break;
                default:
                    LOG.info("Cant detect type of object. " + object);
            }
        } catch (Exception e) {
            LOG.info("Error while send message " + e);
        }
    }

    private String isBotApiMethod(Object object) {
        if (object instanceof BotApiMethod) {
            return "EXECUTE";
        }
        return "NOT_DETECTED";
    }
}
