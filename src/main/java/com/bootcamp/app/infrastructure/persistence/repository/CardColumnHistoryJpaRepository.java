package com.bootcamp.app.infrastructure.persistence.repository;

import com.bootcamp.app.infrastructure.persistence.entity.CardColumnHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardColumnHistoryJpaRepository extends JpaRepository<CardColumnHistoryEntity, Long> {
    List<CardColumnHistoryEntity> findByCardIdOrderByArrivalTimestampAsc(Long cardId);
}
