package com.bootcamp.app.infrastructure.persistence.entity;

import com.bootcamp.app.domain.model.ColumnType;
import jakarta.persistence.*;

@Entity
@Table(name = "board_columns")
public class BoardColumnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity board;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ColumnType type;

    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    public BoardColumnEntity() {}

    public BoardColumnEntity(Long id, BoardEntity board, String name, ColumnType type, int orderIndex) {
        this.id = id;
        this.board = board;
        this.name = name;
        this.type = type;
        this.orderIndex = orderIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BoardEntity getBoard() {
        return board;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}
