package edu.java.controller;

import edu.java.dto.bot.AddLinkRequest;
import edu.java.dto.bot.LinkResponse;
import edu.java.dto.bot.ListLinksResponse;
import edu.java.dto.bot.RemoveLinkRequest;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class ScrapperController {

    @PostMapping("/tg-chat/id")
    public void registerChat(@PathVariable String id) {
        log.info("Registered chat " + id);
    }

    @DeleteMapping("/tg-chat/id")
    public void deleteChat(@PathVariable String id) {
        log.info("Deleted chat " + id);
    }

    @GetMapping("/links")
    public ListLinksResponse getAllLinks(@RequestParam(name = "Tg-Chat-id") String id) {
        ListLinksResponse responce = new ListLinksResponse(List.of());
        log.info("Return all links " + responce + "for chat " + id);
        return responce;
    }

    @PostMapping("/links")
    public LinkResponse addLink(
        @RequestParam(name = "Tg-Chat-Id") String id,
        @RequestBody AddLinkRequest link
    ) {
        log.info("Added link " + link + "to chat id " + id);
        return new LinkResponse(Long.parseLong(id), link.link().toString());
    }

    @DeleteMapping("/links")
    public LinkResponse removeLink(
        @RequestParam(name = "Tg-Chat-Id") String id,
        @RequestBody RemoveLinkRequest link
    ) {
        log.info("Removed link " + link + " from chat " + id);
        return new LinkResponse(Long.parseLong(id), link.uri().toString());
    }
}

