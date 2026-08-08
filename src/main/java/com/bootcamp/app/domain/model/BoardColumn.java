package com.bootcamp.app.domain.model;

import com.bootcamp.app.domain.exception.DomainException;

import java.util.Objects;

public class BoardColumn {
    private Long id;
    private String name;
    private ColumnType type;
    private int orderIndex;

    public BoardColumn(Long id, String name, ColumnType type, int orderIndex) {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainException("O nome da coluna é obrigatório.");
        }
        if (type == null) {
            throw new DomainException("O tipo da coluna é obrigatório.");
        }
        this.id = id;
        this.name = name;
        this.type = type;
        this.orderIndex = orderIndex;
    }

    public BoardColumn(String name, ColumnType type, int orderIndex) {
        this(null, name, type, orderIndex);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ColumnType getType() {
        return type;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardColumn that = (BoardColumn) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }
}
