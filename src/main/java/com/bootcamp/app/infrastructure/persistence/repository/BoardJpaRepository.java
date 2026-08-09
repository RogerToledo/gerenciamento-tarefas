package com.bootcamp.app.infrastructure.persistence.repository;

import com.bootcamp.app.infrastructure.persistence.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardJpaRepository extends JpaRepository<BoardEntity, Long> {

    @Query("SELECT DISTINCT b FROM BoardEntity b JOIN b.columns col JOIN CardEntity c ON c.column.id = col.id WHERE c.id = :cardId")
    Optional<BoardEntity> findByCardId(@Param("cardId") Long cardId);
}
