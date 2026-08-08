package com.bootcamp.app.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "column_id", nullable = false)
    private BoardColumnEntity column;

    @Column(nullable = false)
    private boolean blocked;

    @Column(name = "block_reason", columnDefinition = "TEXT")
    private String blockReason;

    @Column(name = "block_timestamp")
    private LocalDateTime blockTimestamp;

    @Column(name = "unblock_timestamp")
    private LocalDateTime unblockTimestamp;

    public CardEntity() {}

    public CardEntity(Long id, String title, String description, BoardColumnEntity column, boolean blocked, String blockReason, LocalDateTime blockTimestamp, LocalDateTime unblockTimestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.column = column;
        this.blocked = blocked;
        this.blockReason = blockReason;
        this.blockTimestamp = blockTimestamp;
        this.unblockTimestamp = unblockTimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BoardColumnEntity getColumn() {
        return column;
    }

    public void setColumn(BoardColumnEntity column) {
        this.column = column;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getBlockReason() {
        return blockReason;
    }

    public void setBlockReason(String blockReason) {
        this.blockReason = blockReason;
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
