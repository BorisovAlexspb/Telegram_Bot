package edu.java.service.jdbc;

import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcChatService implements ChatService {

    private final JdbcChatRepository chatRepository;

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
