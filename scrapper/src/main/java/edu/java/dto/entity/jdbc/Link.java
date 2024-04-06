package edu.java.dto.entity.jdbc;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    private Integer id;
    private String url;
    private OffsetDateTime updatedAt;
    private OffsetDateTime checkedAt;
    private OffsetDateTime createdAt;
}
