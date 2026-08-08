package com.bootcamp.app.infrastructure.web.controller;

import com.bootcamp.app.application.dto.BoardDTO;
import com.bootcamp.app.application.usecases.BoardUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardUseCase boardUseCase;

    public BoardController(BoardUseCase boardUseCase) {
        this.boardUseCase = boardUseCase;
    }

    @PostMapping
    public ResponseEntity<BoardDTO> createBoard(@RequestParam String name, @RequestParam(required = false) String description) {
        BoardDTO board = boardUseCase.createBoard(name, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(board);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable Long id) {
        return ResponseEntity.ok(boardUseCase.getBoardById(id));
    }

    @GetMapping
    public ResponseEntity<List<BoardDTO>> getAllBoards() {
        return ResponseEntity.ok(boardUseCase.getAllBoards());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardUseCase.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/columns")
    public ResponseEntity<BoardDTO> addPendingColumn(@PathVariable Long id, @RequestParam String columnName, @RequestParam int position) {
        BoardDTO board = boardUseCase.addPendingColumn(id, columnName, position);
        return ResponseEntity.ok(board);
    }

    @DeleteMapping("/{id}/columns/{columnId}")
    public ResponseEntity<BoardDTO> removePendingColumn(@PathVariable Long id, @PathVariable Long columnId) {
        BoardDTO board = boardUseCase.removePendingColumn(id, columnId);
        return ResponseEntity.ok(board);
    }
}
