package com.bootcamp.app.application.ports;

import com.bootcamp.app.domain.model.Board;
import com.bootcamp.app.domain.model.BoardColumn;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryPort {
    Board save(Board board);
    Optional<Board> findById(Long id);
    Optional<Board> findByCardId(Long cardId);
    List<Board> findAll();
    void deleteById(Long id);
    void deleteColumn(Long columnId);
}
