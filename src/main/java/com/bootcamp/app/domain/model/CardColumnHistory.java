package com.bootcamp.app.domain.model;

import java.time.LocalDateTime;

public class CardColumnHistory {
    private Long id;
    private Long cardId;
    private Long columnId;
    private LocalDateTime arrivalTimestamp;
    private LocalDateTime departureTimestamp;

    public CardColumnHistory(Long id, Long cardId, Long columnId, LocalDateTime arrivalTimestamp, LocalDateTime departureTimestamp) {
        this.id = id;
        this.cardId = cardId;
        this.columnId = columnId;
        this.arrivalTimestamp = arrivalTimestamp;
        this.departureTimestamp = departureTimestamp;
    }

    public CardColumnHistory(Long cardId, Long columnId, LocalDateTime arrivalTimestamp) {
        this(null, cardId, columnId, arrivalTimestamp, null);
    }

    public Long getId() {
        return id;
    }

    public Long getCardId() {
        return cardId;
    }

    public Long getColumnId() {
        return columnId;
    }

    public LocalDateTime getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public LocalDateTime getDepartureTimestamp() {
        return departureTimestamp;
    }

    public void setDepartureTimestamp(LocalDateTime departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }
}
