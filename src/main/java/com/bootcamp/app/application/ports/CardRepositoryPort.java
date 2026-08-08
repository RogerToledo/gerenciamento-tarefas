package com.bootcamp.app.application.ports;

import com.bootcamp.app.domain.model.Card;
import com.bootcamp.app.domain.model.CardBlockHistory;
import com.bootcamp.app.domain.model.CardColumnHistory;

import java.util.List;
import java.util.Optional;

public interface CardRepositoryPort {
    Card save(Card card);
    Optional<Card> findById(Long id);
    List<Card> findByBoardId(Long boardId);
    void deleteById(Long id);

    void saveColumnHistory(CardColumnHistory history);
    List<CardColumnHistory> findColumnHistoryByCardId(Long cardId);

    void saveBlockHistory(CardBlockHistory history);
    List<CardBlockHistory> findBlockHistoryByCardId(Long cardId);
    List<CardBlockHistory> findBlockHistoryByBoardId(Long boardId);
}
