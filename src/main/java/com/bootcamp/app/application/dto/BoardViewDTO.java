package com.bootcamp.app.application.dto;

import java.util.List;

public record BoardViewDTO(
        Long boardId,
        String boardName,
        String boardDescription,
        List<ColumnWithCardsDTO> columns
) {}
