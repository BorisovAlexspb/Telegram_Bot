package edu.java.dto.entity.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "chatLinks")
//@EqualsAndHashCode(of = "url")
@Entity
@Table(name = "link")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "URL", unique = true, nullable = false)
    private String url;

    @Column(name = "Checked_at")
    private OffsetDateTime checkedAt;

    @Column(name = "Last_updated")
    private OffsetDateTime lastUpdatedAt;

    @CreatedDate
    @Column(name = "Created_at")
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "link")
    private List<ChatLink> chatLinks = new ArrayList<>();
}
