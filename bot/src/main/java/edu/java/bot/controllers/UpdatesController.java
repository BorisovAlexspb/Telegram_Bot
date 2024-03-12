package edu.java.bot.controllers;

import edu.java.bot.dto.LinkUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
@Log4j2
public class UpdatesController {

    @PostMapping
    public void processUpdate(@RequestBody @Valid LinkUpdateRequest linkUpdateRequest) {
        // TODO: сделать реализацию контроллера
        log.info("Received updates: {} ", linkUpdateRequest);
    }
}
