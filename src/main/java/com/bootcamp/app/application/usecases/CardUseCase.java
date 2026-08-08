package com.bootcamp.app.application.usecases;

import com.bootcamp.app.application.dto.CardDTO;
import com.bootcamp.app.application.ports.BoardRepositoryPort;
import com.bootcamp.app.application.ports.CardRepositoryPort;
import com.bootcamp.app.domain.exception.DomainException;
import com.bootcamp.app.domain.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class CardUseCase {

    private final CardRepositoryPort cardRepository;
    private final BoardRepositoryPort boardRepository;

    public CardUseCase(CardRepositoryPort cardRepository, BoardRepositoryPort boardRepository) {
        this.cardRepository = cardRepository;
        this.boardRepository = boardRepository;
    }

    public CardDTO createCard(Long boardId, String title, String description) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new DomainException("Board não encontrado com ID: " + boardId));

        BoardColumn initialColumn = board.getColumns().stream()
                .filter(c -> c.getType() == ColumnType.INITIAL)
                .findFirst()
                .orElseThrow(() -> new DomainException("Coluna INITIAL não encontrada no board."));

        Card card = new Card(title, description, initialColumn);
        Card savedCard = cardRepository.save(card);

        // Registrar histórico inicial de permanência na coluna
        CardColumnHistory history = new CardColumnHistory(savedCard.getId(), initialColumn.getId(), LocalDateTime.now());
        cardRepository.saveColumnHistory(history);

        return toDTO(savedCard);
    }

    public CardDTO moveCardToNext(Long cardId, Long boardId) {
        Card card = getCardOrThrow(cardId);
        Board board = getBoardOrThrow(boardId);

        int nextOrderIndex = card.getCurrentColumn().getOrderIndex() + 1;
        BoardColumn nextColumn = board.getColumns().stream()
                .filter(c -> c.getOrderIndex() == nextOrderIndex)
                .findFirst()
                .orElseThrow(() -> new DomainException("Não existe próxima coluna no board."));

        LocalDateTime now = LocalDateTime.now();

        // Fechar histórico da coluna atual
        updateCurrentColumnHistory(cardId, card.getCurrentColumn().getId(), now);

        card.moveToNextColumn(nextColumn);
        Card savedCard = cardRepository.save(card);

        // Criar novo histórico para a próxima coluna
        cardRepository.saveColumnHistory(new CardColumnHistory(savedCard.getId(), nextColumn.getId(), now));

        return toDTO(savedCard);
    }

    public CardDTO cancelCard(Long cardId, Long boardId) {
        Card card = getCardOrThrow(cardId);
        Board board = getBoardOrThrow(boardId);

        BoardColumn cancelledColumn = board.getColumns().stream()
                .filter(c -> c.getType() == ColumnType.CANCELLED)
                .findFirst()
                .orElseThrow(() -> new DomainException("Coluna CANCELLED não encontrada no board."));

        LocalDateTime now = LocalDateTime.now();
        updateCurrentColumnHistory(cardId, card.getCurrentColumn().getId(), now);

        card.cancel(cancelledColumn);
        Card savedCard = cardRepository.save(card);

        cardRepository.saveColumnHistory(new CardColumnHistory(savedCard.getId(), cancelledColumn.getId(), now));

        return toDTO(savedCard);
    }

    public CardDTO blockCard(Long cardId, String reason) {
        Card card = getCardOrThrow(cardId);
        card.block(reason);
        Card savedCard = cardRepository.save(card);

        CardBlockHistory blockHistory = new CardBlockHistory(savedCard.getId(), reason, card.getBlockTimestamp());
        cardRepository.saveBlockHistory(blockHistory);

        return toDTO(savedCard);
    }

    public CardDTO unblockCard(Long cardId) {
        Card card = getCardOrThrow(cardId);
        card.unblock();
        Card savedCard = cardRepository.save(card);

        List<CardBlockHistory> histories = cardRepository.findBlockHistoryByCardId(cardId);
        if (!histories.isEmpty()) {
            CardBlockHistory lastBlock = histories.get(histories.size() - 1);
            lastBlock.setUnblockTimestamp(card.getUnblockTimestamp());
            cardRepository.saveBlockHistory(lastBlock);
        }

        return toDTO(savedCard);
    }

    public List<CardDTO> getCardsByBoard(Long boardId) {
        return cardRepository.findByBoardId(boardId).stream()
                .map(this::toDTO)
                .toList();
    }

    private void updateCurrentColumnHistory(Long cardId, Long columnId, LocalDateTime departureTime) {
        List<CardColumnHistory> histories = cardRepository.findColumnHistoryByCardId(cardId);
        histories.stream()
                .filter(h -> h.getColumnId().equals(columnId) && h.getDepartureTimestamp() == null)
                .findFirst()
                .ifPresent(h -> {
                    h.setDepartureTimestamp(departureTime);
                    cardRepository.saveColumnHistory(h);
                });
    }

    private Card getCardOrThrow(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new DomainException("Card não encontrado com ID: " + cardId));
    }

    private Board getBoardOrThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new DomainException("Board não encontrado com ID: " + boardId));
    }

    private CardDTO toDTO(Card card) {
        return new CardDTO(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getCurrentColumn().getId(),
                card.getCurrentColumn().getName(),
                card.isBlocked(),
                card.getBlockReason(),
                card.getBlockTimestamp(),
                card.getUnblockTimestamp()
        );
    }
}
