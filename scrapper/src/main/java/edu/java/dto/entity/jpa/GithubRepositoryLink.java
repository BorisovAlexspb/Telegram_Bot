package edu.java.dto.entity.jpa;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class GithubRepositoryLink extends Link {

    @Builder
    public GithubRepositoryLink(
        Long id,
        String url,
        OffsetDateTime checkedAt,
        OffsetDateTime lastUpdatedAt,
        OffsetDateTime createdAt,
        List<ChatLink> chatLinks
    ) {
        super(id, url, checkedAt, lastUpdatedAt, createdAt, chatLinks);
    }
}
