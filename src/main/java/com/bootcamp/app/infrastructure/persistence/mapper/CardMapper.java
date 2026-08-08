package com.bootcamp.app.infrastructure.persistence.mapper;

import com.bootcamp.app.domain.model.BoardColumn;
import com.bootcamp.app.domain.model.Card;
import com.bootcamp.app.infrastructure.persistence.entity.BoardColumnEntity;
import com.bootcamp.app.infrastructure.persistence.entity.CardEntity;

public class CardMapper {

    public static Card toDomain(CardEntity entity) {
        if (entity == null) return null;

        BoardColumn column = new BoardColumn(
                entity.getColumn().getId(),
                entity.getColumn().getName(),
                entity.getColumn().getType(),
                entity.getColumn().getOrderIndex()
        );

        Card card = new Card(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                column
        );

        if (entity.isBlocked()) {
            card.block(entity.getBlockReason());
        }

        return card;
    }

    public static CardEntity toEntity(Card domain) {
        if (domain == null) return null;

        BoardColumnEntity columnEntity = new BoardColumnEntity();
        columnEntity.setId(domain.getCurrentColumn().getId());
        columnEntity.setName(domain.getCurrentColumn().getName());
        columnEntity.setType(domain.getCurrentColumn().getType());
        columnEntity.setOrderIndex(domain.getCurrentColumn().getOrderIndex());

        return new CardEntity(
                domain.getId(),
                domain.getTitle(),
                domain.getDescription(),
                columnEntity,
                domain.isBlocked(),
                domain.getBlockReason(),
                domain.getBlockTimestamp(),
                domain.getUnblockTimestamp()
        );
    }
}
