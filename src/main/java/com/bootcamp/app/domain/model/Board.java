package com.bootcamp.app.domain.model;

import com.bootcamp.app.domain.exception.DomainException;
import com.bootcamp.app.domain.exception.InvalidColumnOrderException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Board {
    private Long id;
    private String name;
    private String description;
    private final List<BoardColumn> columns = new ArrayList<>();

    public Board(Long id, String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainException("O nome do board é obrigatório.");
        }
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Board(String name, String description) {
        this(null, name, description);
        initDefaultColumns();
    }

    /**
     * Cria as 3 colunas padrão obrigatórias:
     * 1. Backlog (INITIAL)
     * 2. Em andamento (PENDING)
     * 3. Entregue (FINAL)
     */
    private void initDefaultColumns() {
        this.columns.add(new BoardColumn("Backlog", ColumnType.INITIAL, 1));
        this.columns.add(new BoardColumn("Em andamento", ColumnType.PENDING, 2));
        this.columns.add(new BoardColumn("Entregue", ColumnType.FINAL, 3));
    }

    public void addPendingColumn(String columnName, int position) {
        if (columnName == null || columnName.trim().isEmpty()) {
            throw new DomainException("O nome da coluna é obrigatório.");
        }
        // Posições válidas para PENDING são entre a posição 2 e antes de FINAL/CANCELLED
        int initialCount = (int) columns.stream().filter(c -> c.getType() == ColumnType.INITIAL).count();
        if (position <= initialCount || position >= columns.size()) {
            throw new InvalidColumnOrderException("A coluna PENDING só pode ser inserida em posições intermediárias.");
        }

        BoardColumn newColumn = new BoardColumn(columnName, ColumnType.PENDING, position);
        columns.add(position - 1, newColumn);
        reindexColumns();
        validateColumnStructure();
    }

    public void removePendingColumn(BoardColumn column) {
        if (column.getType() != ColumnType.PENDING) {
            throw new InvalidColumnOrderException("Apenas colunas do tipo PENDING podem ser removidas.");
        }
        columns.remove(column);
        reindexColumns();
        validateColumnStructure();
    }

    private void reindexColumns() {
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setOrderIndex(i + 1);
        }
    }

    public void validateColumnStructure() {
        if (columns.isEmpty()) {
            throw new InvalidColumnOrderException("O board deve conter colunas.");
        }

        columns.sort(Comparator.comparingInt(BoardColumn::getOrderIndex));

        // 1. INITIAL deve ser sempre a 1ª coluna
        if (columns.get(0).getType() != ColumnType.INITIAL) {
            throw new InvalidColumnOrderException("A primeira coluna deve ser sempre do tipo INITIAL.");
        }

        long initialCount = columns.stream().filter(c -> c.getType() == ColumnType.INITIAL).count();
        long finalCount = columns.stream().filter(c -> c.getType() == ColumnType.FINAL).count();
        long cancelledCount = columns.stream().filter(c -> c.getType() == ColumnType.CANCELLED).count();

        if (initialCount != 1) {
            throw new InvalidColumnOrderException("O board deve conter exatamente 1 coluna INITIAL.");
        }
        if (finalCount != 1) {
            throw new InvalidColumnOrderException("O board deve conter exatamente 1 coluna FINAL.");
        }
        if (cancelledCount > 1) {
            throw new InvalidColumnOrderException("O board não pode conter mais de 1 coluna CANCELLED.");
        }

        // Se houver CANCELLED, ela deve ser a última e FINAL deve ser a penúltima
        if (cancelledCount == 1) {
            if (columns.get(columns.size() - 1).getType() != ColumnType.CANCELLED) {
                throw new InvalidColumnOrderException("A coluna CANCELLED deve ser sempre a última posição.");
            }
            if (columns.get(columns.size() - 2).getType() != ColumnType.FINAL) {
                throw new InvalidColumnOrderException("A coluna FINAL deve ser a penúltima posição quando houver colunas CANCELLED.");
            }
        } else {
            // Sem CANCELLED, FINAL é a última
            if (columns.get(columns.size() - 1).getType() != ColumnType.FINAL) {
                throw new InvalidColumnOrderException("A coluna FINAL deve ser a última posição na ausência de CANCELLED.");
            }
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<BoardColumn> getColumns() {
        return new ArrayList<>(columns);
    }
}
