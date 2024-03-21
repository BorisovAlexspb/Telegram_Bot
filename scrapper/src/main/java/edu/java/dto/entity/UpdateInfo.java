package edu.java.dto.entity;

import java.time.OffsetDateTime;

public record UpdateInfo(boolean isNewUpdate, OffsetDateTime updateTime, String message) {
}
