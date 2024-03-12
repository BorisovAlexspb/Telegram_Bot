package edu.java.dto.bot;

import java.util.List;

public record ListLinksResponse(
    List<LinkResponse> links,
    int size
) {
    public ListLinksResponse(List<LinkResponse> links) {
        this(links, links.size());
    }
}
