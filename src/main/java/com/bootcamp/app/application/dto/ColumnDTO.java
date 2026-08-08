package com.bootcamp.app.application.dto;

import com.bootcamp.app.domain.model.ColumnType;

public record ColumnDTO(
        Long id,
        String name,
        ColumnType type,
        int orderIndex
) {}
