package edu.java.service.jooq;

import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// тот же код что и в Jdbc сервисах
@Service
@RequiredArgsConstructor
public class JooqChatService implements ChatService {

    private final JooqChatRepository chatRepository;

    @Override
    @Transactional
    public void register(long chatId) {
        if (chatRepository.find(chatId) != null) {
            throw new ChatAlreadyRegisteredException("chat is already registered");
        }
        chatRepository.add(chatId);
    }

    @Override
    @Transactional
    public void unregister(long chatId) {
        if (chatRepository.find(chatId) == null) {
            throw new ChatNotFoundException("chat is not registered");
        }
        chatRepository.remove(chatId);
    }

}

