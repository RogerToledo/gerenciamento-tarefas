package com.bootcamp.app.infrastructure.persistence.adapter;

import com.bootcamp.app.application.ports.CardRepositoryPort;
import com.bootcamp.app.domain.model.Card;
import com.bootcamp.app.domain.model.CardBlockHistory;
import com.bootcamp.app.domain.model.CardColumnHistory;
import com.bootcamp.app.infrastructure.persistence.entity.CardBlockHistoryEntity;
import com.bootcamp.app.infrastructure.persistence.entity.CardColumnHistoryEntity;
import com.bootcamp.app.infrastructure.persistence.entity.CardEntity;
import com.bootcamp.app.infrastructure.persistence.mapper.CardMapper;
import com.bootcamp.app.infrastructure.persistence.repository.CardBlockHistoryJpaRepository;
import com.bootcamp.app.infrastructure.persistence.repository.CardColumnHistoryJpaRepository;
import com.bootcamp.app.infrastructure.persistence.repository.CardJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CardRepositoryAdapter implements CardRepositoryPort {

    private final CardJpaRepository cardJpaRepository;
    private final CardColumnHistoryJpaRepository columnHistoryJpaRepository;
    private final CardBlockHistoryJpaRepository blockHistoryJpaRepository;

    public CardRepositoryAdapter(CardJpaRepository cardJpaRepository,
                                 CardColumnHistoryJpaRepository columnHistoryJpaRepository,
                                 CardBlockHistoryJpaRepository blockHistoryJpaRepository) {
        this.cardJpaRepository = cardJpaRepository;
        this.columnHistoryJpaRepository = columnHistoryJpaRepository;
        this.blockHistoryJpaRepository = blockHistoryJpaRepository;
    }

    @Override
    public Card save(Card card) {
        CardEntity entity = CardMapper.toEntity(card);
        CardEntity saved = cardJpaRepository.save(entity);
        return CardMapper.toDomain(saved);
    }

    @Override
    public Optional<Card> findById(Long id) {
        return cardJpaRepository.findById(id)
                .map(CardMapper::toDomain);
    }

    @Override
    public List<Card> findByBoardId(Long boardId) {
        return cardJpaRepository.findByBoardId(boardId).stream()
                .map(CardMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        cardJpaRepository.deleteById(id);
    }

    @Override
    public void saveColumnHistory(CardColumnHistory history) {
        CardColumnHistoryEntity entity = new CardColumnHistoryEntity(
                history.getId(),
                history.getCardId(),
                history.getColumnId(),
                history.getArrivalTimestamp(),
                history.getDepartureTimestamp()
        );
        columnHistoryJpaRepository.save(entity);
    }

    @Override
    public List<CardColumnHistory> findColumnHistoryByCardId(Long cardId) {
        return columnHistoryJpaRepository.findByCardIdOrderByArrivalTimestampAsc(cardId).stream()
                .map(e -> new CardColumnHistory(
                        e.getId(),
                        e.getCardId(),
                        e.getColumnId(),
                        e.getArrivalTimestamp(),
                        e.getDepartureTimestamp()
                ))
                .toList();
    }

    @Override
    public void saveBlockHistory(CardBlockHistory history) {
        CardBlockHistoryEntity entity = new CardBlockHistoryEntity(
                history.getId(),
                history.getCardId(),
                history.getReason(),
                history.getBlockTimestamp(),
                history.getUnblockTimestamp()
        );
        blockHistoryJpaRepository.save(entity);
    }

    @Override
    public List<CardBlockHistory> findBlockHistoryByCardId(Long cardId) {
        return blockHistoryJpaRepository.findByCardIdOrderByBlockTimestampAsc(cardId).stream()
                .map(e -> new CardBlockHistory(
                        e.getId(),
                        e.getCardId(),
                        e.getReason(),
                        e.getBlockTimestamp(),
                        e.getUnblockTimestamp()
                ))
                .toList();
    }

    @Override
    public List<CardBlockHistory> findBlockHistoryByBoardId(Long boardId) {
        return blockHistoryJpaRepository.findByBoardId(boardId).stream()
                .map(e -> new CardBlockHistory(
                        e.getId(),
                        e.getCardId(),
                        e.getReason(),
                        e.getBlockTimestamp(),
                        e.getUnblockTimestamp()
                ))
                .toList();
    }
}
