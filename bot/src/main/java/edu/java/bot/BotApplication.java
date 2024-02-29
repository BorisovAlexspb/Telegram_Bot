package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.MessageReciever;
import edu.java.bot.service.MessageSender;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {

    //private static final Logger log = Logger.getLogger(BotApplication.class.getName());
    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 3;
    private static final String BOT_ADMIN = "";

    public static void main(String[] args) throws TelegramApiException {

        Bot testBot = new Bot("myBot", System.getenv("APP_TELEGRAM_TOKEN"));
        MessageReciever messageReciever = new MessageReciever(testBot);
        MessageSender messageSender = new MessageSender(testBot);
        testBot.botConnect();

        Thread receiver = new Thread(messageReciever);
        receiver.setDaemon(true);
        receiver.setName("MsgReciever");
        receiver.setPriority(PRIORITY_FOR_RECEIVER);
        receiver.start();

        Thread sender = new Thread(messageSender);
        sender.setDaemon(true);
        sender.setName("MsgSender");
        sender.setPriority(PRIORITY_FOR_SENDER);
        sender.start();

        sendStartReport(testBot);
    }

    private static void sendStartReport(Bot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(BOT_ADMIN);
        sendMessage.setText("Запустился");
        bot.sendQueue.add(sendMessage);
    }
}
