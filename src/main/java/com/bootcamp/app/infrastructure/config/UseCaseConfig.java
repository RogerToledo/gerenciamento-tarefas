package com.bootcamp.app.infrastructure.config;

import com.bootcamp.app.application.ports.BoardRepositoryPort;
import com.bootcamp.app.application.ports.CardRepositoryPort;
import com.bootcamp.app.application.usecases.BoardUseCase;
import com.bootcamp.app.application.usecases.CardUseCase;
import com.bootcamp.app.application.usecases.ReportUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public BoardUseCase boardUseCase(BoardRepositoryPort boardRepositoryPort) {
        return new BoardUseCase(boardRepositoryPort);
    }

    @Bean
    public CardUseCase cardUseCase(CardRepositoryPort cardRepositoryPort, BoardRepositoryPort boardRepositoryPort) {
        return new CardUseCase(cardRepositoryPort, boardRepositoryPort);
    }

    @Bean
    public ReportUseCase reportUseCase(CardRepositoryPort cardRepositoryPort, BoardRepositoryPort boardRepositoryPort) {
        return new ReportUseCase(cardRepositoryPort, boardRepositoryPort);
    }

    @Bean
    public com.bootcamp.app.application.usecases.ViewBoardUseCase viewBoardUseCase(BoardRepositoryPort boardRepositoryPort, CardRepositoryPort cardRepositoryPort) {
        return new com.bootcamp.app.application.usecases.ViewBoardUseCase(boardRepositoryPort, cardRepositoryPort);
    }
}
