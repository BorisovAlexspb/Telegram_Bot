package edu.java.dto.entity;

public enum LinkType {

    GITHUB_REPO("https://github.com"),
    STACKOVERFLOW_QUESTION("https://stackoverflow.com"),

    UNKNOWN("Unknown url pattern");

    private final String urlPattern;

    LinkType(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public static LinkType getTypeOfLink(String url) {
        if (url.startsWith(GITHUB_REPO.urlPattern)) {
            return GITHUB_REPO;
        } else if (url.startsWith(STACKOVERFLOW_QUESTION.urlPattern)) {
            return STACKOVERFLOW_QUESTION;
        }
        return UNKNOWN;
    }

}
