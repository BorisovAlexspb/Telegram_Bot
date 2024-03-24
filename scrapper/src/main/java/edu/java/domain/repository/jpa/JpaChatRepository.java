package edu.java.domain.repository.jpa;

import edu.java.dto.entity.jpa.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {
}
