package edu.java.bot.controllers;

import edu.java.bot.dto.LinkUpdateRequest;
import edu.java.bot.service.BotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class UpdatesController {

    private BotService botService;

    public UpdatesController(BotService botService) {
        this.botService = botService;
    }

    @PostMapping
    public ResponseEntity<String> processUpdate(@RequestBody @Valid LinkUpdateRequest linkUpdateRequest) {
        botService.sendUpdate(linkUpdateRequest);
        return ResponseEntity.ok("Обновление обработано");
    }
}
