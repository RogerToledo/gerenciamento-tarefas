package com.bootcamp.app.infrastructure.cli;

import com.bootcamp.app.application.dto.*;
import com.bootcamp.app.application.usecases.BoardUseCase;
import com.bootcamp.app.application.usecases.CardUseCase;
import com.bootcamp.app.application.usecases.ReportUseCase;
import com.bootcamp.app.application.usecases.ViewBoardUseCase;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
@Profile("!test")
public class BoardCliApplication implements CommandLineRunner {

    private final BoardUseCase boardUseCase;
    private final CardUseCase cardUseCase;
    private final ReportUseCase reportUseCase;
    private final ViewBoardUseCase viewBoardUseCase;

    public BoardCliApplication(BoardUseCase boardUseCase, CardUseCase cardUseCase, ReportUseCase reportUseCase, ViewBoardUseCase viewBoardUseCase) {
        this.boardUseCase = boardUseCase;
        this.cardUseCase = cardUseCase;
        this.reportUseCase = reportUseCase;
        this.viewBoardUseCase = viewBoardUseCase;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=========================================");
        System.out.println(" 📋 BEM-VINDO AO BOARD DE TAREFAS (CLI)");
        System.out.println("=========================================");

        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Criar novo board");
            System.out.println("2. Selecionar board");
            System.out.println("3. Excluir board");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");

            String option = scanner.nextLine().trim();

            try {
                switch (option) {
                    case "1" -> handleCreateBoard(scanner);
                    case "2" -> handleSelectBoard(scanner);
                    case "3" -> handleDeleteBoard(scanner);
                    case "4" -> {
                        running = false;
                        System.out.println("\nSaindo do sistema. Até logo!");
                    }
                    default -> System.out.println("⚠️ Opção inválida. Tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("❌ Erro: " + e.getMessage());
            }
        }
    }

    private void handleCreateBoard(Scanner scanner) {
        System.out.print("Digite o nome do board: ");
        String name = scanner.nextLine().trim();
        System.out.print("Digite a descrição do board (opcional): ");
        String description = scanner.nextLine().trim();

        BoardDTO board = boardUseCase.createBoard(name, description);
        System.out.println("✅ Board criado com sucesso! [ID: " + board.id() + "]");
    }

    private void handleSelectBoard(Scanner scanner) {
        List<BoardDTO> boards = boardUseCase.getAllBoards();
        if (boards.isEmpty()) {
            System.out.println("ℹ️ Nenhum board cadastrado.");
            return;
        }

        System.out.println("\nBoards disponíveis:");
        boards.forEach(b -> System.out.println("  ID " + b.id() + " - " + b.name()));

        System.out.print("Digite o ID do board que deseja selecionar: ");
        Long boardId = Long.parseLong(scanner.nextLine().trim());

        BoardDTO selectedBoard = boardUseCase.getBoardById(boardId);
        runBoardMenu(scanner, selectedBoard.id());
    }

    private void handleDeleteBoard(Scanner scanner) {
        System.out.print("Digite o ID do board que deseja excluir: ");
        Long boardId = Long.parseLong(scanner.nextLine().trim());
        boardUseCase.deleteBoard(boardId);
        System.out.println("✅ Board excluído com sucesso!");
    }

    private void runBoardMenu(Scanner scanner, Long boardId) {
        boolean inBoardMenu = true;
        while (inBoardMenu) {
            BoardViewDTO boardView = viewBoardUseCase.execute(boardId);
            BoardConsolePrinter.printBoard(boardView);

            System.out.println("\n--- MENU DO BOARD ---");
            System.out.println("1. Mover card para próxima coluna");
            System.out.println("2. Cancelar card");
            System.out.println("3. Criar card");
            System.out.println("4. Bloquear card");
            System.out.println("5. Desbloquear card");
            System.out.println("6. Gerar relatório de tempo");
            System.out.println("7. Gerar relatório de bloqueios");
            System.out.println("8. Fechar board (Voltar ao Menu Principal)");
            System.out.print("Escolha uma opção: ");

            String option = scanner.nextLine().trim();

            try {
                switch (option) {
                    case "1" -> handleMoveCard(scanner, boardId);
                    case "2" -> handleCancelCard(scanner, boardId);
                    case "3" -> handleCreateCard(scanner, boardId);
                    case "4" -> handleBlockCard(scanner);
                    case "5" -> handleUnblockCard(scanner);
                    case "6" -> handleTimeReport(scanner);
                    case "7" -> handleBlockReport(boardId);
                    case "8" -> inBoardMenu = false;
                    default -> System.out.println("⚠️ Opção inválida. Tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("❌ Erro: " + e.getMessage());
            }
        }
    }

    private void handleCreateCard(Scanner scanner, Long boardId) {
        System.out.print("Digite o título do card: ");
        String title = scanner.nextLine().trim();
        System.out.print("Digite a descrição do card (opcional): ");
        String description = scanner.nextLine().trim();

        CardDTO card = cardUseCase.createCard(boardId, title, description);
        System.out.println("✅ Card criado com sucesso! [ID: " + card.id() + "]");
    }

    private void handleMoveCard(Scanner scanner, Long boardId) {
        System.out.print("Digite o ID do card a ser movido: ");
        Long cardId = Long.parseLong(scanner.nextLine().trim());
        CardDTO card = cardUseCase.moveCardToNext(cardId, boardId);
        System.out.println("✅ Card movido para a coluna: " + card.columnName());
    }

    private void handleCancelCard(Scanner scanner, Long boardId) {
        System.out.print("Digite o ID do card a ser cancelado: ");
        Long cardId = Long.parseLong(scanner.nextLine().trim());
        CardDTO card = cardUseCase.cancelCard(cardId, boardId);
        System.out.println("✅ Card cancelado com sucesso!");
    }

    private void handleBlockCard(Scanner scanner) {
        System.out.print("Digite o ID do card a ser bloqueado: ");
        Long cardId = Long.parseLong(scanner.nextLine().trim());
        System.out.print("Digite a justificativa de bloqueio (obrigatório): ");
        String reason = scanner.nextLine().trim();

        CardDTO card = cardUseCase.blockCard(cardId, reason);
        System.out.println("🔒 Card bloqueado com sucesso!");
    }

    private void handleUnblockCard(Scanner scanner) {
        System.out.print("Digite o ID do card a ser desbloqueado: ");
        Long cardId = Long.parseLong(scanner.nextLine().trim());

        CardDTO card = cardUseCase.unblockCard(cardId);
        System.out.println("🔓 Card desbloqueado com sucesso!");
    }

    private void handleTimeReport(Scanner scanner) {
        System.out.print("Digite o ID do card para relatório de tempo: ");
        Long cardId = Long.parseLong(scanner.nextLine().trim());

        CardTimeReportDTO report = reportUseCase.getCardTimeReport(cardId);
        System.out.println("\n📊 RELATÓRIO DE TEMPO DO CARD: " + report.cardTitle());
        System.out.println("Tempo total: " + report.totalTimeInMinutes() + " minuto(s)");
        report.columnTimes().forEach(ct ->
                System.out.println(" - " + ct.columnName() + ": " + ct.timeInMinutes() + " min(s)")
        );
    }

    private void handleBlockReport(Long boardId) {
        List<BlockReportDTO> reports = reportUseCase.getBoardBlockReport(boardId);
        System.out.println("\n📊 RELATÓRIO DE BLOQUEIOS DO BOARD");
        if (reports.isEmpty()) {
            System.out.println("Nenhum histórico de bloqueio encontrado neste board.");
            return;
        }

        reports.forEach(r ->
                System.out.println(" - Card: " + r.cardTitle() + " | Motivo: " + r.reason() + " | Duração: " + r.blockDurationInMinutes() + " min(s)")
        );
    }
}
