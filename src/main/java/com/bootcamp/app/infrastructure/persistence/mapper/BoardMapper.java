package com.bootcamp.app.infrastructure.persistence.mapper;

import com.bootcamp.app.domain.model.Board;
import com.bootcamp.app.domain.model.BoardColumn;
import com.bootcamp.app.infrastructure.persistence.entity.BoardColumnEntity;
import com.bootcamp.app.infrastructure.persistence.entity.BoardEntity;

import java.util.ArrayList;
import java.util.List;

public class BoardMapper {

    public static Board toDomain(BoardEntity entity) {
        if (entity == null) return null;

        List<BoardColumn> domainColumns = new ArrayList<>();
        if (entity.getColumns() != null) {
            for (BoardColumnEntity colEntity : entity.getColumns()) {
                domainColumns.add(new BoardColumn(
                        colEntity.getId(),
                        colEntity.getName(),
                        colEntity.getType(),
                        colEntity.getOrderIndex()
                ));
            }
        }

        return new Board(entity.getId(), entity.getName(), entity.getDescription(), domainColumns);
    }

    public static BoardEntity toEntity(Board domain) {
        if (domain == null) return null;

        BoardEntity entity = new BoardEntity(domain.getId(), domain.getName(), domain.getDescription());
        if (domain.getColumns() != null) {
            for (BoardColumn colDomain : domain.getColumns()) {
                BoardColumnEntity colEntity = new BoardColumnEntity(
                        colDomain.getId(),
                        entity,
                        colDomain.getName(),
                        colDomain.getType(),
                        colDomain.getOrderIndex()
                );
                entity.addColumn(colEntity);
            }
        }
        return entity;
    }
}
