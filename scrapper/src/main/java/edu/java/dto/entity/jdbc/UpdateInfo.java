package edu.java.dto.entity.jdbc;

import java.time.OffsetDateTime;

public record UpdateInfo(boolean isNewUpdate, OffsetDateTime updateTime, String message) {
}
