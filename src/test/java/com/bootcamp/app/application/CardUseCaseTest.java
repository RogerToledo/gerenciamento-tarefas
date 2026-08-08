package com.bootcamp.app.application;

import com.bootcamp.app.application.dto.CardDTO;
import com.bootcamp.app.application.ports.BoardRepositoryPort;
import com.bootcamp.app.application.ports.CardRepositoryPort;
import com.bootcamp.app.application.usecases.CardUseCase;
import com.bootcamp.app.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CardUseCaseTest {

    private CardRepositoryPort cardRepository;
    private BoardRepositoryPort boardRepository;
    private CardUseCase cardUseCase;

    private Board board;
    private Card card;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepositoryPort.class);
        boardRepository = mock(BoardRepositoryPort.class);
        cardUseCase = new CardUseCase(cardRepository, boardRepository);

        board = new Board(1L, "Board Teste", "Descrição");
        BoardColumn colInitial = board.getColumns().get(0);

        card = new Card(10L, "Nova Funcionalidade", "Detalhes", colInitial);
    }

    @Test
    @DisplayName("Deve criar card e registrar histórico de coluna")
    void shouldCreateCard() {
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        CardDTO result = cardUseCase.createCard(1L, "Nova Funcionalidade", "Detalhes");

        assertNotNull(result);
        assertEquals("Nova Funcionalidade", result.title());
        verify(cardRepository, times(1)).saveColumnHistory(any(CardColumnHistory.class));
    }

    @Test
    @DisplayName("Deve mover card para a próxima coluna")
    void shouldMoveCardToNextColumn() {
        when(cardRepository.findById(10L)).thenReturn(Optional.of(card));
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        CardDTO result = cardUseCase.moveCardToNext(10L, 1L);

        assertNotNull(result);
        verify(cardRepository, times(1)).save(any(Card.class));
        verify(cardRepository, times(1)).saveColumnHistory(any(CardColumnHistory.class));
    }

    @Test
    @DisplayName("Deve bloquear o card e registrar justificativa")
    void shouldBlockCard() {
        when(cardRepository.findById(10L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        CardDTO result = cardUseCase.blockCard(10L, "Aguardando API externa");

        assertNotNull(result);
        verify(cardRepository, times(1)).saveBlockHistory(any(CardBlockHistory.class));
    }
}
