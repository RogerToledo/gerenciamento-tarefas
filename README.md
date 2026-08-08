# 📋 Board de Tarefas Customizável (Kanban)

Aplicação backend em Java 21 / Spring Boot 3 para gerenciamento e acompanhamento de tarefas no formato de Board (Kanban), com suporte a colunas customizáveis, movimentação sequencial de cards, controle rigoroso de bloqueios, relatório de métricas e interface CLI e REST.

---

## 🏛️ Arquitetura do Projeto (Clean Architecture)

O projeto adota os princípios de **Clean Architecture** e **Clean Code**, garantindo alto desacoplamento e testabilidade:

```text
src/main/java/com/bootcamp/app/
├── domain/                      # [Core] Regras de negócio puras (Sem anotações de frameworks)
│   ├── model/                   # Entidades (Board, BoardColumn, Card, Históricos)
│   └── exception/               # Exceções de domínio customizadas
├── application/                 # [Casos de Uso]
│   ├── dto/                     # DTOs de entrada e saída (Java Records)
│   ├── ports/                   # Interfaces dos adaptadores/repositórios
│   └── usecases/                # Lógica dos casos de uso (BoardUseCase, CardUseCase, ReportUseCase)
└── infrastructure/              # [Detalhes de Implementação]
    ├── cli/                     # Interface de usuário via linha de comando (Console CLI)
    ├── config/                  # Beans de configuração do Spring
    ├── persistence/             # Flyway, Spring Data JPA, Entities e Mappers
    └── web/                     # Controllers REST API
```

---

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 21 (LTS) / Java 25
* **Framework Web:** Spring Boot 3.3.4
* **Persistência / ORM:** Spring Data JPA / Hibernate
* **Banco de Dados:** MySQL 8.0 (Executado via Docker) / H2 (para ambiente de testes)
* **Database Migrations:** Flyway
* **Testes:** JUnit 5 & Mockito
* **Build Tool:** Maven

---

## 🐳 Como Executar o Banco de Dados (Docker)

Certifique-se de ter o Docker e Docker Compose instalados. Na raiz do projeto, execute:

```bash
docker compose -f compose-mysql.yml up -d
```

O container MySQL 8.0 subirá na porta `3306` com o banco `board_db` criado automaticamente.

---

## 🛠️ Como Executar a Aplicação

### 1. Compilar e Executar via Maven

```bash
mvn clean spring-boot:run
```

Ao iniciar, a aplicação carregará o **Menu Interativo no Console (CLI)** diretamente no terminal:

```text
=========================================
 📋 BEM-VINDO AO BOARD DE TAREFAS (CLI)
=========================================

--- MENU PRINCIPAL ---
1. Criar novo board
2. Selecionar board
3. Excluir board
4. Sair
Escolha uma opção:
```

---

## 🧪 Como Executar os Testes

A suíte inclui testes unitários do domínio, testes de casos de uso com Mockito e testes de integração de ponta a ponta:

```bash
mvn clean test
```

---

## 🌐 Endpoints REST API

A aplicação também disponibiliza uma API REST para integração externa:

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/boards?name={nome}&description={desc}` | Criar novo Board |
| `GET` | `/api/boards` | Listar todos os Boards |
| `GET` | `/api/boards/{id}` | Buscar Board por ID |
| `DELETE` | `/api/boards/{id}` | Excluir Board por ID |
| `POST` | `/api/boards/{id}/columns?columnName={nome}&position={pos}` | Adicionar coluna customizada PENDING |
| `POST` | `/api/cards?boardId={id}&title={titulo}` | Criar novo Card no Backlog |
| `PUT` | `/api/cards/{id}/move?boardId={boardId}` | Mover Card para a próxima coluna |
| `PUT` | `/api/cards/{id}/cancel?boardId={boardId}` | Cancelar Card |
| `PUT` | `/api/cards/{id}/block?reason={justificativa}` | Bloquear Card com justificativa |
| `PUT` | `/api/cards/{id}/unblock` | Desbloquear Card |
| `GET` | `/api/reports/cards/{cardId}/time` | Relatório de tempo do Card em colunas |
| `GET` | `/api/reports/boards/{boardId}/blocks` | Relatório de bloqueios do Board |

---

## 📜 Licença

Projeto desenvolvido para fins educacionais e de avaliação do Bootcamp.
