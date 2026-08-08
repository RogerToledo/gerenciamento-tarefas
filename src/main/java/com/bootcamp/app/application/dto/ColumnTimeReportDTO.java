package com.bootcamp.app.application.dto;

import java.time.LocalDateTime;

public record ColumnTimeReportDTO(
        String columnName,
        LocalDateTime arrivalTimestamp,
        LocalDateTime departureTimestamp,
        long timeInMinutes
) {}
