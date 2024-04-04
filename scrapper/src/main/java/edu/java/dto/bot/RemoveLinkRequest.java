package edu.java.dto.bot;

import jakarta.validation.constraints.NotNull;

public record RemoveLinkRequest(
    @NotNull String link
) {
}
