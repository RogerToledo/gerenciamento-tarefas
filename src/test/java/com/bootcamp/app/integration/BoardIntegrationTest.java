package com.bootcamp.app.integration;

import com.bootcamp.app.application.dto.BlockReportDTO;
import com.bootcamp.app.application.dto.BoardDTO;
import com.bootcamp.app.application.dto.CardDTO;
import com.bootcamp.app.application.dto.CardTimeReportDTO;
import com.bootcamp.app.application.usecases.BoardUseCase;
import com.bootcamp.app.application.usecases.CardUseCase;
import com.bootcamp.app.application.usecases.ReportUseCase;
import com.bootcamp.app.domain.exception.CardBlockedException;
import com.bootcamp.app.domain.exception.InvalidCardMovementException;
import com.bootcamp.app.domain.model.ColumnType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BoardIntegrationTest {

    @Autowired
    private BoardUseCase boardUseCase;

    @Autowired
    private CardUseCase cardUseCase;

    @Autowired
    private ReportUseCase reportUseCase;

    @Test
    @DisplayName("Fluxo Completo: Criar Board -> Criar Card -> Mover até Entregue -> Gerar Relatório de Tempo")
    void fullBoardCardLifecycleIntegrationTest() {
        // 1. Criar Board
        BoardDTO board = boardUseCase.createBoard("Projeto Integração", "Testando fluxo completo");
        assertNotNull(board.id());
        assertEquals(4, board.columns().size());

        // 2. Criar Card
        CardDTO card = cardUseCase.createCard(board.id(), "Implementar Login OAuth2", "Com suporte a Google e GitHub");
        assertNotNull(card.id());
        assertEquals("Backlog", card.columnName());

        // 3. Mover Card do Backlog para Em andamento
        CardDTO movedCard1 = cardUseCase.moveCardToNext(card.id(), board.id());
        assertEquals("Em andamento", movedCard1.columnName());

        // 4. Mover Card de Em andamento para Entregue
        CardDTO movedCard2 = cardUseCase.moveCardToNext(card.id(), board.id());
        assertEquals("Entregue", movedCard2.columnName());

        // 5. Gerar Relatório de Tempo em Colunas
        CardTimeReportDTO timeReport = reportUseCase.getCardTimeReport(card.id());
        assertNotNull(timeReport);
        assertEquals(card.id(), timeReport.cardId());
        assertEquals("Implementar Login OAuth2", timeReport.cardTitle());
        assertEquals(3, timeReport.columnTimes().size());
    }

    @Test
    @DisplayName("Cenário de Bloqueio: Criar Card -> Bloquear -> Tentar Mover (Erro) -> Desbloquear -> Relatório de Bloqueios")
    void cardBlockingAndReportIntegrationTest() {
        BoardDTO board = boardUseCase.createBoard("Board Bloqueios", "Testando bloqueio de cards");
        CardDTO card = cardUseCase.createCard(board.id(), "Ajuste na API de Pagamento", "Corrigir webhook");

        // Bloquear Card
        CardDTO blockedCard = cardUseCase.blockCard(card.id(), "Aguardando homologação da Adquirente");
        assertTrue(blockedCard.blocked());
        assertEquals("Aguardando homologação da Adquirente", blockedCard.blockReason());

        // Tentar mover card bloqueado deve falhar
        assertThrows(CardBlockedException.class, () -> cardUseCase.moveCardToNext(card.id(), board.id()));

        // Desbloquear Card
        CardDTO unblockedCard = cardUseCase.unblockCard(card.id());
        assertFalse(unblockedCard.blocked());

        // Mover card desbloqueado deve ter sucesso
        CardDTO movedCard = cardUseCase.moveCardToNext(card.id(), board.id());
        assertEquals("Em andamento", movedCard.columnName());

        // Gerar Relatório de Bloqueios do Board
        List<BlockReportDTO> blockReports = reportUseCase.getBoardBlockReport(board.id());
        assertNotNull(blockReports);
        assertEquals(1, blockReports.size());
        assertEquals("Aguardando homologação da Adquirente", blockReports.get(0).reason());
    }

    @Test
    @DisplayName("Customização de Colunas PENDING e Cancelamento de Card")
    void pendingColumnsAndCancellationIntegrationTest() {
        BoardDTO board = boardUseCase.createBoard("Board Customizado", "Com colunas customizadas");

        // Adicionar coluna PENDING customizada na posição 2
        BoardDTO updatedBoard = boardUseCase.addPendingColumn(board.id(), "Code Review", 2);
        assertEquals(5, updatedBoard.columns().size());
        assertEquals("Code Review", updatedBoard.columns().get(1).name());

        // Criar Card e Mover para Code Review
        CardDTO card = cardUseCase.createCard(board.id(), "Refatorar Mappers", "Clean Architecture");
        CardDTO movedToCodeReview = cardUseCase.moveCardToNext(card.id(), board.id());
        assertEquals("Code Review", movedToCodeReview.columnName());

        // Adicionar coluna CANCELLED ao board
        boardUseCase.addPendingColumn(board.id(), "Aguardando Deploy", 3);

        // Cancelar Card
        CardDTO cancelledCard = cardUseCase.cancelCard(card.id(), board.id());
        assertNotNull(cancelledCard);
    }
}
