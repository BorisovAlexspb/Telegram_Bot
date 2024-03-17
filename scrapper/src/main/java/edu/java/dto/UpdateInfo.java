package edu.java.dto;

import java.time.OffsetDateTime;

public record UpdateInfo(boolean isNewUpdate, OffsetDateTime updateTime, String message) {
}
