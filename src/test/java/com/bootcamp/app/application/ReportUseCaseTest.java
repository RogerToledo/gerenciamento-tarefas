package com.bootcamp.app.application;

import com.bootcamp.app.application.dto.BlockReportDTO;
import com.bootcamp.app.application.dto.CardTimeReportDTO;
import com.bootcamp.app.application.ports.BoardRepositoryPort;
import com.bootcamp.app.application.ports.CardRepositoryPort;
import com.bootcamp.app.application.usecases.ReportUseCase;
import com.bootcamp.app.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportUseCaseTest {

    private CardRepositoryPort cardRepository;
    private BoardRepositoryPort boardRepository;
    private ReportUseCase reportUseCase;

    private Board board;
    private Card card;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepositoryPort.class);
        boardRepository = mock(BoardRepositoryPort.class);
        reportUseCase = new ReportUseCase(cardRepository, boardRepository);

        board = new Board(1L, "Board Teste", "Descrição");
        card = new Card(10L, "Task 1", "Descrição", board.getColumns().get(0));
    }

    @Test
    @DisplayName("Deve gerar relatório de tempo gasto nas colunas pelo card")
    void shouldGetCardTimeReport() {
        LocalDateTime now = LocalDateTime.now();
        CardColumnHistory history1 = new CardColumnHistory(1L, 10L, 1L, now.minusMinutes(30), now);

        when(cardRepository.findById(10L)).thenReturn(Optional.of(card));
        when(cardRepository.findColumnHistoryByCardId(10L)).thenReturn(List.of(history1));

        CardTimeReportDTO report = reportUseCase.getCardTimeReport(10L);

        assertNotNull(report);
        assertEquals(10L, report.cardId());
        assertEquals(30, report.totalTimeInMinutes());
        assertEquals(1, report.columnTimes().size());
    }

    @Test
    @DisplayName("Deve gerar relatório de histórico de bloqueios do board")
    void shouldGetBoardBlockReport() {
        LocalDateTime now = LocalDateTime.now();
        CardBlockHistory blockHistory = new CardBlockHistory(1L, 10L, "Aguardando aprovação", now.minusMinutes(45), now);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(cardRepository.findBlockHistoryByBoardId(1L)).thenReturn(List.of(blockHistory));
        when(cardRepository.findByBoardId(1L)).thenReturn(List.of(card));

        List<BlockReportDTO> reports = reportUseCase.getBoardBlockReport(1L);

        assertNotNull(reports);
        assertEquals(1, reports.size());
        assertEquals("Aguardando aprovação", reports.get(0).reason());
        assertEquals(45, reports.get(0).blockDurationInMinutes());
    }
}
