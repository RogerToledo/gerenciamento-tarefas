package com.bootcamp.app.infrastructure.web.controller;

import com.bootcamp.app.application.dto.CardDTO;
import com.bootcamp.app.application.usecases.CardUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardUseCase cardUseCase;

    public CardController(CardUseCase cardUseCase) {
        this.cardUseCase = cardUseCase;
    }

    @PostMapping
    public ResponseEntity<CardDTO> createCard(@RequestParam Long boardId, @RequestParam String title, @RequestParam(required = false) String description) {
        CardDTO card = cardUseCase.createCard(boardId, title, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }

    @PutMapping("/{id}/move")
    public ResponseEntity<CardDTO> moveCardToNext(@PathVariable Long id, @RequestParam Long boardId) {
        CardDTO card = cardUseCase.moveCardToNext(id, boardId);
        return ResponseEntity.ok(card);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<CardDTO> cancelCard(@PathVariable Long id, @RequestParam Long boardId) {
        CardDTO card = cardUseCase.cancelCard(id, boardId);
        return ResponseEntity.ok(card);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<CardDTO> blockCard(@PathVariable Long id, @RequestParam String reason) {
        CardDTO card = cardUseCase.blockCard(id, reason);
        return ResponseEntity.ok(card);
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<CardDTO> unblockCard(@PathVariable Long id) {
        CardDTO card = cardUseCase.unblockCard(id);
        return ResponseEntity.ok(card);
    }

    @GetMapping
    public ResponseEntity<List<CardDTO>> getCardsByBoard(@RequestParam Long boardId) {
        return ResponseEntity.ok(cardUseCase.getCardsByBoard(boardId));
    }
}
