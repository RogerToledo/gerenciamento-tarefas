package com.bootcamp.app.application.usecases;

import com.bootcamp.app.application.dto.BlockReportDTO;
import com.bootcamp.app.application.dto.CardTimeReportDTO;
import com.bootcamp.app.application.dto.ColumnTimeReportDTO;
import com.bootcamp.app.application.ports.BoardRepositoryPort;
import com.bootcamp.app.application.ports.CardRepositoryPort;
import com.bootcamp.app.domain.exception.DomainException;
import com.bootcamp.app.domain.model.Board;
import com.bootcamp.app.domain.model.BoardColumn;
import com.bootcamp.app.domain.model.Card;
import com.bootcamp.app.domain.model.CardBlockHistory;
import com.bootcamp.app.domain.model.CardColumnHistory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportUseCase {

    private final CardRepositoryPort cardRepository;
    private final BoardRepositoryPort boardRepository;

    public ReportUseCase(CardRepositoryPort cardRepository, BoardRepositoryPort boardRepository) {
        this.cardRepository = cardRepository;
        this.boardRepository = boardRepository;
    }

    public CardTimeReportDTO getCardTimeReport(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new DomainException("Card não encontrado com ID: " + cardId));

        Map<Long, String> columnNameMap = boardRepository.findByCardId(cardId)
                .map(b -> b.getColumns().stream()
                        .collect(Collectors.toMap(BoardColumn::getId, BoardColumn::getName, (a, bName) -> a)))
                .orElse(Map.of());

        List<CardColumnHistory> histories = cardRepository.findColumnHistoryByCardId(cardId);
        List<ColumnTimeReportDTO> columnTimes = new ArrayList<>();
        long totalMinutes = 0;

        for (CardColumnHistory history : histories) {
            LocalDateTime arrival = history.getArrivalTimestamp();
            LocalDateTime departure = history.getDepartureTimestamp() != null ? history.getDepartureTimestamp() : LocalDateTime.now();

            long minutes = Duration.between(arrival, departure).toMinutes();
            totalMinutes += minutes;

            String columnName = columnNameMap.getOrDefault(history.getColumnId(), "Coluna #" + history.getColumnId());
            columnTimes.add(new ColumnTimeReportDTO(columnName, arrival, history.getDepartureTimestamp(), minutes));
        }

        return new CardTimeReportDTO(card.getId(), card.getTitle(), columnTimes, totalMinutes);
    }

    public List<BlockReportDTO> getBoardBlockReport(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new DomainException("Board não encontrado com ID: " + boardId));

        List<CardBlockHistory> blockHistories = cardRepository.findBlockHistoryByBoardId(boardId);
        List<Card> boardCards = cardRepository.findByBoardId(boardId);
        Map<Long, String> cardTitleMap = boardCards.stream()
                .collect(Collectors.toMap(Card::getId, Card::getTitle, (a, b) -> a));

        return blockHistories.stream()
                .map(bh -> {
                    LocalDateTime end = bh.getUnblockTimestamp() != null ? bh.getUnblockTimestamp() : LocalDateTime.now();
                    long duration = Duration.between(bh.getBlockTimestamp(), end).toMinutes();
                    String title = cardTitleMap.getOrDefault(bh.getCardId(), "Card #" + bh.getCardId());

                    return new BlockReportDTO(
                            bh.getCardId(),
                            title,
                            bh.getReason(),
                            bh.getBlockTimestamp(),
                            bh.getUnblockTimestamp(),
                            duration
                    );
                })
                .toList();
    }
}
