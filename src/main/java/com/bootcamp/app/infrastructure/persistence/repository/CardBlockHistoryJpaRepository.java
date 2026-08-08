package com.bootcamp.app.infrastructure.persistence.repository;

import com.bootcamp.app.infrastructure.persistence.entity.CardBlockHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardBlockHistoryJpaRepository extends JpaRepository<CardBlockHistoryEntity, Long> {
    List<CardBlockHistoryEntity> findByCardIdOrderByBlockTimestampAsc(Long cardId);

    @Query("SELECT cbh FROM CardBlockHistoryEntity cbh WHERE cbh.cardId IN (SELECT c.id FROM CardEntity c WHERE c.column.board.id = :boardId) ORDER BY cbh.blockTimestamp ASC")
    List<CardBlockHistoryEntity> findByBoardId(@Param("boardId") Long boardId);
}
