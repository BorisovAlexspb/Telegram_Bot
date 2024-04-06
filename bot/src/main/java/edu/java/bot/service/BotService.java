package edu.java.bot.service;

import edu.java.bot.Bot;
import edu.java.bot.dto.LinkUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class BotService {

    private final Bot bot;

    public BotService(Bot bot) {
        this.bot = bot;
    }

    public void sendUpdate(LinkUpdateRequest updateRequest) {
        StringBuilder stringBuilder = new StringBuilder("Пришло обновление!\n");
        stringBuilder.append(updateRequest.id())
            .append(": ")
            .append(updateRequest.url())
            .append("\nОписание:\n")
            .append(updateRequest.description());

        for (Long chatId : updateRequest.tgChatIds()) {
            try {
                bot.execute(new SendMessage(chatId.toString(), stringBuilder.toString()));
            } catch (TelegramApiException e) {
                log.info(e.getMessage());
            }

        }
    }
}
