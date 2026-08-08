package com.bootcamp.app.application;

import com.bootcamp.app.application.dto.BoardDTO;
import com.bootcamp.app.application.ports.BoardRepositoryPort;
import com.bootcamp.app.application.usecases.BoardUseCase;
import com.bootcamp.app.domain.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BoardUseCaseTest {

    private BoardRepositoryPort boardRepository;
    private BoardUseCase boardUseCase;

    @BeforeEach
    void setUp() {
        boardRepository = mock(BoardRepositoryPort.class);
        boardUseCase = new BoardUseCase(boardRepository);
    }

    @Test
    @DisplayName("Deve criar um novo board com sucesso")
    void shouldCreateBoardSuccessfully() {
        Board savedBoard = new Board(1L, "Projeto Principal", "Descrição");
        when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

        BoardDTO result = boardUseCase.createBoard("Projeto Principal", "Descrição");

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Projeto Principal", result.name());
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("Deve buscar um board por id")
    void shouldGetBoardById() {
        Board board = new Board(1L, "Projeto Principal", "Descrição");
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        BoardDTO result = boardUseCase.getBoardById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(boardRepository, times(1)).findById(1L);
    }
}
