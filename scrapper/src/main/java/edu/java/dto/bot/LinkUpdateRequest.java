package edu.java.dto.bot;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record LinkUpdateRequest(
    @NotNull Long id,
    @NotNull String url,
    @NotEmpty String description,
    @NotEmpty List<Long> tgChatIds
) {
}
