package edu.java.controller;

import edu.java.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<String> registerChat(@PathVariable Long id) {
        chatService.register(id);
        return ResponseEntity.ok("Чат зарегистрирован");
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<String> deleteChat(@PathVariable Long id) {
        chatService.unregister(id);
        return ResponseEntity.ok("Чат успешно удалён");
    }
}
