package com.bootcamp.app.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "card_block_history")
public class CardBlockHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "block_timestamp", nullable = false)
    private LocalDateTime blockTimestamp;

    @Column(name = "unblock_timestamp")
    private LocalDateTime unblockTimestamp;

    public CardBlockHistoryEntity() {}

    public CardBlockHistoryEntity(Long id, Long cardId, String reason, LocalDateTime blockTimestamp, LocalDateTime unblockTimestamp) {
        this.id = id;
        this.cardId = cardId;
        this.reason = reason;
        this.blockTimestamp = blockTimestamp;
        this.unblockTimestamp = unblockTimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getBlockTimestamp() {
        return blockTimestamp;
    }

    public void setBlockTimestamp(LocalDateTime blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

    public LocalDateTime getUnblockTimestamp() {
        return unblockTimestamp;
    }

    public void setUnblockTimestamp(LocalDateTime unblockTimestamp) {
        this.unblockTimestamp = unblockTimestamp;
    }
}
