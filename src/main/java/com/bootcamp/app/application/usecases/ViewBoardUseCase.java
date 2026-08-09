package com.bootcamp.app.application.usecases;

import com.bootcamp.app.application.dto.BoardViewDTO;
import com.bootcamp.app.application.dto.CardDTO;
import com.bootcamp.app.application.dto.ColumnWithCardsDTO;
import com.bootcamp.app.application.ports.BoardRepositoryPort;
import com.bootcamp.app.application.ports.CardRepositoryPort;
import com.bootcamp.app.domain.exception.DomainException;
import com.bootcamp.app.domain.model.Board;
import com.bootcamp.app.domain.model.Card;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewBoardUseCase {

    private final BoardRepositoryPort boardRepository;
    private final CardRepositoryPort cardRepository;

    public ViewBoardUseCase(BoardRepositoryPort boardRepository, CardRepositoryPort cardRepository) {
        this.boardRepository = boardRepository;
        this.cardRepository = cardRepository;
    }

    public BoardViewDTO execute(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new DomainException("Board não encontrado com ID: " + boardId));

        List<Card> cards = cardRepository.findByBoardId(boardId);

        // Agrupa os cards pelo ID da coluna atual
        Map<Long, List<CardDTO>> cardsByColumnId = cards.stream()
                .map(card -> new CardDTO(
                        card.getId(),
                        card.getTitle(),
                        card.getDescription(),
                        card.getCurrentColumn().getId(),
                        card.getCurrentColumn().getName(),
                        card.isBlocked(),
                        card.getBlockReason(),
                        card.getBlockTimestamp(),
                        card.getUnblockTimestamp()
                ))
                .collect(Collectors.groupingBy(CardDTO::columnId));

        // Mapeia e ordena as colunas pelo orderIndex
        List<ColumnWithCardsDTO> columnsWithCards = board.getColumns().stream()
                .sorted(Comparator.comparingInt(com.bootcamp.app.domain.model.BoardColumn::getOrderIndex))
                .map(col -> {
                    List<CardDTO> colCards = cardsByColumnId.getOrDefault(col.getId(), List.of());
                    return new ColumnWithCardsDTO(
                            col.getId(),
                            col.getName(),
                            col.getType(),
                            col.getOrderIndex(),
                            colCards
                    );
                })
                .toList();

        return new BoardViewDTO(
                board.getId(),
                board.getName(),
                board.getDescription(),
                columnsWithCards
        );
    }
}
