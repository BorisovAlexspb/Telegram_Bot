package edu.java.service.jpa;

import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.dto.entity.jpa.Chat;
import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.service.ChatService;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {

    private final JpaChatRepository jpaChatRepository;

    @Override
    public void register(long chatId) {
        if (jpaChatRepository.existsById(chatId)) {
            throw new ChatAlreadyRegisteredException("chat is already registered");
        }
        jpaChatRepository.save(Chat.builder().id(chatId).createdAt(OffsetDateTime.now()).build());
    }

    @Override
    public void unregister(long chatId) {
        jpaChatRepository.findById(chatId)
            .ifPresentOrElse(
                    jpaChatRepository::delete,
                () -> {
                    throw new ChatNotFoundException("chat is not registered");
                }
            );
    }
}
