package edu.java.service;

import edu.java.dto.bot.AddLinkRequest;
import edu.java.dto.bot.LinkResponse;
import edu.java.dto.bot.ListLinksResponse;
import edu.java.dto.bot.RemoveLinkRequest;

public interface LinkService {

    LinkResponse add(long chatId, AddLinkRequest addLinkRequest);

    LinkResponse remove(long chatId, RemoveLinkRequest removeLinkRequest);

    ListLinksResponse listAll(long chatId);
}
