package com.bootcamp.app.application.dto;

import java.util.List;

public record CardTimeReportDTO(
        Long cardId,
        String cardTitle,
        List<ColumnTimeReportDTO> columnTimes,
        long totalTimeInMinutes
) {}
