package com.bootcamp.app.application.dto;

import java.util.List;

public record BoardDTO(
        Long id,
        String name,
        String description,
        List<ColumnDTO> columns
) {}
