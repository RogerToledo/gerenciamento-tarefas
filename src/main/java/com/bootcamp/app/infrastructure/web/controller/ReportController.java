package com.bootcamp.app.infrastructure.web.controller;

import com.bootcamp.app.application.dto.BlockReportDTO;
import com.bootcamp.app.application.dto.CardTimeReportDTO;
import com.bootcamp.app.application.usecases.ReportUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportUseCase reportUseCase;

    public ReportController(ReportUseCase reportUseCase) {
        this.reportUseCase = reportUseCase;
    }

    @GetMapping("/cards/{cardId}/time")
    public ResponseEntity<CardTimeReportDTO> getCardTimeReport(@PathVariable Long cardId) {
        return ResponseEntity.ok(reportUseCase.getCardTimeReport(cardId));
    }

    @GetMapping("/boards/{boardId}/blocks")
    public ResponseEntity<List<BlockReportDTO>> getBoardBlockReport(@PathVariable Long boardId) {
        return ResponseEntity.ok(reportUseCase.getBoardBlockReport(boardId));
    }
}
