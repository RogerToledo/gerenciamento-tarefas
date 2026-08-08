package com.bootcamp.app.infrastructure.persistence.repository;

import com.bootcamp.app.infrastructure.persistence.entity.BoardColumnEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardColumnJpaRepository extends JpaRepository<BoardColumnEntity, Long> {
}
