package com.bootcamp.app.domain;

import com.bootcamp.app.domain.exception.DomainException;
import com.bootcamp.app.domain.exception.InvalidColumnOrderException;
import com.bootcamp.app.domain.model.Board;
import com.bootcamp.app.domain.model.ColumnType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    @DisplayName("Deve criar board com as colunas padrão na ordem estrita: INITIAL, PENDING, FINAL, CANCELLED")
    void shouldCreateBoardWithDefaultColumns() {
        Board board = new Board("Projeto Alpha", "Descrição do projeto");

        assertNotNull(board);
        assertEquals("Projeto Alpha", board.getName());
        assertEquals(4, board.getColumns().size());

        assertEquals(ColumnType.INITIAL, board.getColumns().get(0).getType());
        assertEquals("Backlog", board.getColumns().get(0).getName());

        assertEquals(ColumnType.PENDING, board.getColumns().get(1).getType());
        assertEquals("Em andamento", board.getColumns().get(1).getName());

        assertEquals(ColumnType.FINAL, board.getColumns().get(2).getType());
        assertEquals("Entregue", board.getColumns().get(2).getName());

        assertEquals(ColumnType.CANCELLED, board.getColumns().get(3).getType());
        assertEquals("Cancelado", board.getColumns().get(3).getName());
    }

    @Test
    @DisplayName("Deve permitir adicionar coluna PENDING em posição intermediária")
    void shouldAddPendingColumnInIntermediatePosition() {
        Board board = new Board("Projeto Beta", "Board com colunas customizadas");

        board.addPendingColumn("Code Review", 2);

        assertEquals(5, board.getColumns().size());
        assertEquals("Backlog", board.getColumns().get(0).getName());
        assertEquals("Code Review", board.getColumns().get(1).getName());
        assertEquals("Em andamento", board.getColumns().get(2).getName());
        assertEquals("Entregue", board.getColumns().get(3).getName());
        assertEquals("Cancelado", board.getColumns().get(4).getName());
    }

    @Test
    @DisplayName("Lança exceção se tentar criar board sem nome")
    void shouldThrowExceptionWhenBoardNameIsEmpty() {
        DomainException exception = assertThrows(DomainException.class, () -> new Board("", "Descrição"));
        assertEquals("O nome do board é obrigatório.", exception.getMessage());
    }

    @Test
    @DisplayName("Lança exceção se tentar adicionar coluna PENDING na posição 1")
    void shouldThrowExceptionWhenAddingPendingAtFirstPosition() {
        Board board = new Board("Projeto Gamma", null);

        assertThrows(InvalidColumnOrderException.class, () -> board.addPendingColumn("Nova Coluna", 1));
    }
}
