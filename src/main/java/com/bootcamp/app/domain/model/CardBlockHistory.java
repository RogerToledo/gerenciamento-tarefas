package com.bootcamp.app.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class CardBlockHistory {
    private Long id;
    private Long cardId;
    private String reason;
    private LocalDateTime blockTimestamp;
    private LocalDateTime unblockTimestamp;

    public CardBlockHistory(Long id, Long cardId, String reason, LocalDateTime blockTimestamp, LocalDateTime unblockTimestamp) {
        this.id = id;
        this.cardId = cardId;
        this.reason = reason;
        this.blockTimestamp = blockTimestamp;
        this.unblockTimestamp = unblockTimestamp;
    }

    public CardBlockHistory(Long cardId, String reason, LocalDateTime blockTimestamp) {
        this(null, cardId, reason, blockTimestamp, null);
    }

    public Long getId() {
        return id;
    }

    public Long getCardId() {
        return cardId;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getBlockTimestamp() {
        return blockTimestamp;
    }

    public LocalDateTime getUnblockTimestamp() {
        return unblockTimestamp;
    }

    public void setUnblockTimestamp(LocalDateTime unblockTimestamp) {
        this.unblockTimestamp = unblockTimestamp;
    }

    public Duration getBlockDuration() {
        if (blockTimestamp != null && unblockTimestamp != null) {
            return Duration.between(blockTimestamp, unblockTimestamp);
        }
        return Duration.ZERO;
    }
}
