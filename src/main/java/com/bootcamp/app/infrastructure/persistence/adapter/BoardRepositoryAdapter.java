package com.bootcamp.app.infrastructure.persistence.adapter;

import com.bootcamp.app.application.ports.BoardRepositoryPort;
import com.bootcamp.app.domain.model.Board;
import com.bootcamp.app.infrastructure.persistence.entity.BoardEntity;
import com.bootcamp.app.infrastructure.persistence.mapper.BoardMapper;
import com.bootcamp.app.infrastructure.persistence.repository.BoardColumnJpaRepository;
import com.bootcamp.app.infrastructure.persistence.repository.BoardJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BoardRepositoryAdapter implements BoardRepositoryPort {

    private final BoardJpaRepository boardJpaRepository;
    private final BoardColumnJpaRepository columnJpaRepository;

    public BoardRepositoryAdapter(BoardJpaRepository boardJpaRepository, BoardColumnJpaRepository columnJpaRepository) {
        this.boardJpaRepository = boardJpaRepository;
        this.columnJpaRepository = columnJpaRepository;
    }

    @Override
    public Board save(Board board) {
        BoardEntity entity = BoardMapper.toEntity(board);
        BoardEntity saved = boardJpaRepository.save(entity);
        return BoardMapper.toDomain(saved);
    }

    @Override
    public Optional<Board> findById(Long id) {
        return boardJpaRepository.findById(id)
                .map(BoardMapper::toDomain);
    }

    @Override
    public List<Board> findAll() {
        return boardJpaRepository.findAll().stream()
                .map(BoardMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        boardJpaRepository.deleteById(id);
    }

    @Override
    public void deleteColumn(Long columnId) {
        columnJpaRepository.deleteById(columnId);
    }
}
