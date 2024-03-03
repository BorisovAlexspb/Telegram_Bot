package edu.java.dto.bot;

import java.util.List;

public record ListLinksResponce(
    List<LinkResponce> links,
    int size
) {
    public ListLinksResponce(List<LinkResponce> links) {
        this(links, links.size());
    }
}
