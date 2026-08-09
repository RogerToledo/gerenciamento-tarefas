# 🏗️ Especificação de Arquitetura (Clean Architecture)

## 🛠️ Stack Tecnológica

| Categoria | Tecnologia | Descrição / Detalhes |
| :--- | :--- | :--- |
| **Linguagem** | Java 25 | Versão mais recente do Java (uso de Records, Pattern Matching, Sealed Classes) |
| **Framework Web** | Spring Boot 3.x | Framework base para a criação da API REST e Injeção de Dependências |
| **Gerenciador de Build**| Maven | Gestão de dependências e automação de build |
| **Banco de Dados** | MySQL 8.0 / H2 | Banco relacional (MySQL em Produção/Docker, H2 em Memória para Testes) |
| **Persistência / ORM / DAOs** | Spring Data JPA / Hibernate | Mapeamento objeto-relacional, Repositories e Acesso a Dados |
| **Migrações de Banco** | Flyway | Versionamento e evolução do schema do banco de dados |
| **Testes** | JUnit 5, Mockito & MockMvc | Testes unitários, de componentes e de integração E2E |

---

## 🏗️ Arquitetura de Código (Clean Architecture)

O projeto adota estritamente os princípios de **Clean Architecture** (Arquitetura Limpa) e **Clean Code**, garantindo alto desacoplamento, testabilidade e independência de frameworks. O domínio da aplicação é isolado e não possui dependências de bibliotecas externas.

### Estrutura de Pacotes (`src/main/java/com/bootcamp/app/`):

```text
├── domain/                               # [Core] Regras de negócio puras (sem dependências externas)
│   ├── model/                            # Entidades de Domínio (Board, BoardColumn, Card, CardColumnHistory, CardBlockHistory)
│   └── exception/                        # Exceções de domínio (DomainException, InvalidColumnOrderException, CardBlockedException)
│
├── application/                          # [Casos de Uso e Portas]
│   ├── usecases/                         # Orquestração das regras de negócio (BoardUseCase, CardUseCase, ReportUseCase, ViewBoardUseCase)
│   ├── dto/                              # Records imutáveis de transferência de dados (DTOs)
│   └── ports/                            # Interfaces de Saída / Gateways (BoardRepositoryPort, CardRepositoryPort, ColumnRepositoryPort)
│
└── infrastructure/                       # [Detalhes de Implementação / Adaptadores]
    ├── web/                              # REST Controllers e tratamento global de erros (@RestControllerAdvice)
    ├── cli/                              # Interface via Terminal (BoardCliApplication, BoardConsolePrinter)
    ├── persistence/                      # Camada de Acesso a Dados e Persistência
    │   ├── entity/                       # Entidades JPA (@Entity, Mapeamento Relacional)
    │   ├── repository/                   # Repositórios / DAOs do Spring Data JPA (BoardJpaRepository, CardJpaRepository, etc.)
    │   ├── mapper/                       # Mappers de conversão entre Domínio ↔ Entidades JPA/DAO
    │   └── adapter/                      # Adaptadores concretos das Portas (BoardRepositoryAdapter, CardRepositoryAdapter)
    └── config/                           # Beans do Spring Framework e configurações de contexto
```

---

## 🔄 Fluxo de Comunicação entre Camadas (Dependency Rule)

1. **Entrada de Dados (UI / Web / CLI)**: Recebe a requisição do usuário e invoca um **Caso de Uso** (`application.usecases`).
2. **Caso de Uso (`application.usecases`)**: Aplica a orquestração e invoca regras de negócio nas **Entidades de Domínio** (`domain.model`).
3. **Portas de Repositório (`application.ports`)**: Interfaces definidas na camada de aplicação para abstrair o acesso a dados.
4. **Adaptadores de Persistência (`infrastructure.persistence.adapter`)**: Implementam as **Portas de Repositório**, utilizam os Mappers para converter entre Domínio e JPA/DAO, e delegam o acesso ao banco para as interfaces do Spring Data JPA.
