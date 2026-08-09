package com.bootcamp.app.application.dto;

import com.bootcamp.app.domain.model.ColumnType;
import java.util.List;

public record ColumnWithCardsDTO(
        Long columnId,
        String columnName,
        ColumnType columnType,
        int orderIndex,
        List<CardDTO> cards
) {}
