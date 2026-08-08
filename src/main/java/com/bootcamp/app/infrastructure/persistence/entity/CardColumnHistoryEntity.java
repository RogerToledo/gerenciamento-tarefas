package com.bootcamp.app.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "card_column_history")
public class CardColumnHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @Column(name = "column_id", nullable = false)
    private Long columnId;

    @Column(name = "arrival_timestamp", nullable = false)
    private LocalDateTime arrivalTimestamp;

    @Column(name = "departure_timestamp")
    private LocalDateTime departureTimestamp;

    public CardColumnHistoryEntity() {}

    public CardColumnHistoryEntity(Long id, Long cardId, Long columnId, LocalDateTime arrivalTimestamp, LocalDateTime departureTimestamp) {
        this.id = id;
        this.cardId = cardId;
        this.columnId = columnId;
        this.arrivalTimestamp = arrivalTimestamp;
        this.departureTimestamp = departureTimestamp;
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

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public LocalDateTime getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public void setArrivalTimestamp(LocalDateTime arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
    }

    public LocalDateTime getDepartureTimestamp() {
        return departureTimestamp;
    }

    public void setDepartureTimestamp(LocalDateTime departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }
}
