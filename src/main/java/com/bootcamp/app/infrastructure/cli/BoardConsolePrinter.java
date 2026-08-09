package com.bootcamp.app.infrastructure.cli;

import com.bootcamp.app.application.dto.BoardViewDTO;
import com.bootcamp.app.application.dto.CardDTO;
import com.bootcamp.app.application.dto.ColumnWithCardsDTO;

public class BoardConsolePrinter {

    public static void printBoard(BoardViewDTO boardView) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(" 📊 QUADRO KANBAN: " + boardView.boardName().toUpperCase() + " [ID: " + boardView.boardId() + "]");
        if (boardView.boardDescription() != null && !boardView.boardDescription().isBlank()) {
            System.out.println(" 📝 Descrição: " + boardView.boardDescription());
        }
        System.out.println("=".repeat(70));

        for (ColumnWithCardsDTO col : boardView.columns()) {
            System.out.println("\n┌------------------------------------------------------------------");
            System.out.printf("│ 📂 Coluna [%d]: %s (%s) - Total: %d card(s)%n",
                    col.orderIndex(), col.columnName(), col.columnType(), col.cards().size());
            System.out.println("├------------------------------------------------------------------");

            if (col.cards().isEmpty()) {
                System.out.println("│  (Nenhum card nesta coluna)");
            } else {
                for (CardDTO card : col.cards()) {
                    String status = card.blocked() ? " 🔒 [BLOQUEADO]" : " 🟢 [ATIVO]";
                    System.out.printf("│  • [#%d] %s%s%n", card.id(), card.title(), status);
                    if (card.description() != null && !card.description().isBlank()) {
                        System.out.println("│    └─ " + card.description());
                    }
                    if (card.blocked()) {
                        System.out.println("│    └─ Motivo: " + card.blockReason());
                    }
                }
            }
            System.out.println("└------------------------------------------------------------------");
        }
        System.out.println("=".repeat(70) + "\n");
    }
}
