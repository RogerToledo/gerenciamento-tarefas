package com.bootcamp.app.domain.model;

import com.bootcamp.app.domain.exception.CardBlockedException;
import com.bootcamp.app.domain.exception.DomainException;
import com.bootcamp.app.domain.exception.InvalidCardMovementException;

import java.time.LocalDateTime;

public class Card {
    private Long id;
    private String title;
    private String description;
    private BoardColumn currentColumn;
    private boolean blocked;
    private String blockReason;
    private LocalDateTime blockTimestamp;
    private LocalDateTime unblockTimestamp;

    public Card(Long id, String title, String description, BoardColumn currentColumn) {
        if (title == null || title.trim().isEmpty()) {
            throw new DomainException("O título do card é obrigatório.");
        }
        if (currentColumn == null) {
            throw new DomainException("A coluna inicial do card é obrigatória.");
        }
        this.id = id;
        this.title = title;
        this.description = description;
        this.currentColumn = currentColumn;
        this.blocked = false;
    }

    public Card(String title, String description, BoardColumn currentColumn) {
        this(null, title, description, currentColumn);
    }

    public void moveToNextColumn(BoardColumn nextColumn) {
        if (blocked) {
            throw new CardBlockedException("Não é possível mover um card bloqueado. Desbloqueie-o primeiro.");
        }
        if (nextColumn == null) {
            throw new InvalidCardMovementException("A coluna de destino é inválida.");
        }

        // Regra de movimentação sequencial: nextColumn deve ter orderIndex == currentColumn.orderIndex + 1
        if (nextColumn.getOrderIndex() != currentColumn.getOrderIndex() + 1) {
            throw new InvalidCardMovementException("O card só pode mover para a próxima coluna imediatamente à frente.");
        }

        this.currentColumn = nextColumn;
    }

    public void cancel(BoardColumn cancelledColumn) {
        if (blocked) {
            throw new CardBlockedException("Não é possível cancelar um card bloqueado. Desbloqueie-o primeiro.");
        }
        if (currentColumn.getType() == ColumnType.FINAL) {
            throw new InvalidCardMovementException("Não é possível cancelar um card que já está na coluna FINAL.");
        }
        if (cancelledColumn == null || cancelledColumn.getType() != ColumnType.CANCELLED) {
            throw new InvalidCardMovementException("A coluna informada deve ser do tipo CANCELLED.");
        }

        this.currentColumn = cancelledColumn;
    }

    public void block(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new DomainException("A justificativa de bloqueio é obrigatória.");
        }
        if (this.blocked) {
            throw new DomainException("O card já se encontra bloqueado.");
        }
        this.blocked = true;
        this.blockReason = reason;
        this.blockTimestamp = LocalDateTime.now();
        this.unblockTimestamp = null;
    }

    public void unblock() {
        if (!this.blocked) {
            throw new DomainException("O card não está bloqueado.");
        }
        this.blocked = false;
        this.unblockTimestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BoardColumn getCurrentColumn() {
        return currentColumn;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public String getBlockReason() {
        return blockReason;
    }

    public LocalDateTime getBlockTimestamp() {
        return blockTimestamp;
    }

    public LocalDateTime getUnblockTimestamp() {
        return unblockTimestamp;
    }
}
