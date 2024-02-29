package edu.java.bot;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SuppressWarnings("ImportOrder")
@NoArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private static final Logger LOG = Logger.getLogger(Bot.class.getName());

    private static final int RECONNECT_PAUSE = 10000;

    @Setter
    @Getter
    String botName;
    @Setter
    @Getter
    String botToken;
    public final Queue<Object> sendQueue = new ConcurrentLinkedQueue<>();
    public final Queue<Object> receiveQueue = new ConcurrentLinkedQueue<>();

    public Bot(String botName, String botToken) {
        this.botName = botName;
        this.botToken = botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        LOG.info("Receive new Update. updateID: " + update.getUpdateId());
        receiveQueue.add(update);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void botConnect() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(this);
            LOG.info("[STARTED] TelegramAPI. Bot Connected. Bot class: " + this);
        } catch (TelegramApiException e) {
            LOG.info("Cant Connect. Pause " + RECONNECT_PAUSE + "sec and try again. Error: " + e.getMessage());
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException ex) {
                ex.getMessage();
                return;
            }
            botConnect();
        }
    }
}
