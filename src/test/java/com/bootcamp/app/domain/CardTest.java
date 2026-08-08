package com.bootcamp.app.domain;

import com.bootcamp.app.domain.exception.CardBlockedException;
import com.bootcamp.app.domain.exception.DomainException;
import com.bootcamp.app.domain.exception.InvalidCardMovementException;
import com.bootcamp.app.domain.model.BoardColumn;
import com.bootcamp.app.domain.model.Card;
import com.bootcamp.app.domain.model.ColumnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private BoardColumn initialCol;
    private BoardColumn pendingCol;
    private BoardColumn finalCol;
    private BoardColumn cancelledCol;

    @BeforeEach
    void setUp() {
        initialCol = new BoardColumn(1L, "Backlog", ColumnType.INITIAL, 1);
        pendingCol = new BoardColumn(2L, "Em andamento", ColumnType.PENDING, 2);
        finalCol = new BoardColumn(3L, "Entregue", ColumnType.FINAL, 3);
        cancelledCol = new BoardColumn(4L, "Cancelado", ColumnType.CANCELLED, 4);
    }

    @Test
    @DisplayName("Deve mover card sequencialmente para a próxima coluna")
    void shouldMoveCardToNextColumn() {
        Card card = new Card("Implementar Login", "Criar tela e API", initialCol);

        card.moveToNextColumn(pendingCol);

        assertEquals(pendingCol, card.getCurrentColumn());
    }

    @Test
    @DisplayName("Lança exceção se tentar pular coluna ao mover card")
    void shouldThrowExceptionWhenSkippingColumn() {
        Card card = new Card("Implementar Login", "Criar tela e API", initialCol);

        assertThrows(InvalidCardMovementException.class, () -> card.moveToNextColumn(finalCol));
    }

    @Test
    @DisplayName("Deve permitir cancelar card de qualquer coluna exceto FINAL")
    void shouldCancelCardFromPendingColumn() {
        Card card = new Card("Implementar Login", "Criar tela e API", pendingCol);

        card.cancel(cancelledCol);

        assertEquals(cancelledCol, card.getCurrentColumn());
    }

    @Test
    @DisplayName("Lança exceção se tentar cancelar card que já está em FINAL")
    void shouldThrowExceptionWhenCancellingCardFromFinalColumn() {
        Card card = new Card("Implementar Login", "Criar tela e API", finalCol);

        assertThrows(InvalidCardMovementException.class, () -> card.cancel(cancelledCol));
    }

    @Test
    @DisplayName("Deve bloquear e impedir movimentação do card")
    void shouldBlockCardAndPreventMovement() {
        Card card = new Card("Ajustar Bug", "Correção urgente", initialCol);

        card.block("Aguardando aprovação de layout pelo cliente");

        assertTrue(card.isBlocked());
        assertEquals("Aguardando aprovação de layout pelo cliente", card.getBlockReason());
        assertNotNull(card.getBlockTimestamp());

        assertThrows(CardBlockedException.class, () -> card.moveToNextColumn(pendingCol));
    }

    @Test
    @DisplayName("Lança exceção se tentar bloquear card sem justificativa")
    void shouldThrowExceptionWhenBlockingWithoutReason() {
        Card card = new Card("Ajustar Bug", "Correção urgente", initialCol);

        assertThrows(DomainException.class, () -> card.block("   "));
    }

    @Test
    @DisplayName("Deve desbloquear card e permitir movimentação normalmente")
    void shouldUnblockCardAndAllowMovement() {
        Card card = new Card("Ajustar Bug", "Correção urgente", initialCol);
        card.block("Aguardando aprovação");

        card.unblock();

        assertFalse(card.isBlocked());
        assertNotNull(card.getUnblockTimestamp());

        card.moveToNextColumn(pendingCol);
        assertEquals(pendingCol, card.getCurrentColumn());
    }
}
