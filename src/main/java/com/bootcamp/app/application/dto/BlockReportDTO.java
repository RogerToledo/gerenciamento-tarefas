package com.bootcamp.app.application.dto;

import java.time.LocalDateTime;

public record BlockReportDTO(
        Long cardId,
        String cardTitle,
        String reason,
        LocalDateTime blockTimestamp,
        LocalDateTime unblockTimestamp,
        long blockDurationInMinutes
) {}
