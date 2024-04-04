package edu.java.controller;

import edu.java.dto.bot.AddLinkRequest;
import edu.java.dto.bot.LinkResponse;
import edu.java.dto.bot.ListLinksResponse;
import edu.java.dto.bot.RemoveLinkRequest;
import edu.java.service.LinkService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/links")
    public ListLinksResponse getAllLinks(@RequestHeader(name = "Tg-Chat-id") Long id) {
        return linkService.listAll(id);
    }

    @PostMapping("/links")
    public LinkResponse addLink(
        @RequestHeader(name = "Tg-Chat-Id") Long id,
        @RequestBody AddLinkRequest link
    ) {
        return linkService.add(id, link);
    }

    @DeleteMapping("/links")
    public LinkResponse removeLink(
        @RequestHeader(name = "Tg-Chat-Id") Long id,
        @RequestBody RemoveLinkRequest link
    ) {
        return linkService.remove(id, link);
    }
}

