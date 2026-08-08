package com.bootcamp.app.application.usecases;

import com.bootcamp.app.application.dto.BoardDTO;
import com.bootcamp.app.application.dto.ColumnDTO;
import com.bootcamp.app.application.ports.BoardRepositoryPort;
import com.bootcamp.app.domain.exception.DomainException;
import com.bootcamp.app.domain.model.Board;
import com.bootcamp.app.domain.model.BoardColumn;

import java.util.List;

public class BoardUseCase {

    private final BoardRepositoryPort boardRepository;

    public BoardUseCase(BoardRepositoryPort boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardDTO createBoard(String name, String description) {
        Board board = new Board(name, description);
        Board saved = boardRepository.save(board);
        return toDTO(saved);
    }

    public BoardDTO getBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new DomainException("Board não encontrado com ID: " + id));
        return toDTO(board);
    }

    public List<BoardDTO> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public void deleteBoard(Long id) {
        boardRepository.findById(id)
                .orElseThrow(() -> new DomainException("Board não encontrado para exclusão com ID: " + id));
        boardRepository.deleteById(id);
    }

    public BoardDTO addPendingColumn(Long boardId, String columnName, int position) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new DomainException("Board não encontrado com ID: " + boardId));

        board.addPendingColumn(columnName, position);
        Board saved = boardRepository.save(board);
        return toDTO(saved);
    }

    public BoardDTO removePendingColumn(Long boardId, Long columnId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new DomainException("Board não encontrado com ID: " + boardId));

        BoardColumn columnToRemove = board.getColumns().stream()
                .filter(c -> c.getId() != null && c.getId().equals(columnId))
                .findFirst()
                .orElseThrow(() -> new DomainException("Coluna não encontrada no board com ID: " + columnId));

        board.removePendingColumn(columnToRemove);
        boardRepository.deleteColumn(columnId);
        Board saved = boardRepository.save(board);
        return toDTO(saved);
    }

    private BoardDTO toDTO(Board board) {
        List<ColumnDTO> columns = board.getColumns().stream()
                .map(c -> new ColumnDTO(c.getId(), c.getName(), c.getType(), c.getOrderIndex()))
                .toList();
        return new BoardDTO(board.getId(), board.getName(), board.getDescription(), columns);
    }
}
