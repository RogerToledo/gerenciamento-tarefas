package com.bootcamp.app.application.dto;

import java.time.LocalDateTime;

public record CardDTO(
        Long id,
        String title,
        String description,
        Long columnId,
        String columnName,
        boolean blocked,
        String blockReason,
        LocalDateTime blockTimestamp,
        LocalDateTime unblockTimestamp
) {}
