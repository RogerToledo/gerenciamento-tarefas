package com.bootcamp.app.infrastructure.persistence.repository;

import com.bootcamp.app.infrastructure.persistence.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardJpaRepository extends JpaRepository<CardEntity, Long> {

    @Query("SELECT c FROM CardEntity c WHERE c.column.board.id = :boardId")
    List<CardEntity> findByBoardId(@Param("boardId") Long boardId);
}
