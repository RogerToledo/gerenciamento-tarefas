package com.bootcamp.app.infrastructure.persistence.repository;

import com.bootcamp.app.infrastructure.persistence.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJpaRepository extends JpaRepository<BoardEntity, Long> {
}
