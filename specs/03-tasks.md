# 📋 Plano de Execução e Checklist de Tarefas (Task-Driven)

Este documento guia o desenvolvimento passo a passo da aplicação de Board de Tarefas. Execute uma tarefa por vez no Antigravity, garanta que os testes e validações passem e marque o checkbox `[x]` antes de prosseguir.

---

## 🚀 Fase 1: Setup do Projeto e Infraestrutura

- [x] **Task 1.1:** Criar/Inicializar o projeto Java 25 com Spring Boot, configurando as dependências base (`Spring Data JPA`, `MySQL Driver`, `Validation`, `Flyway`).
- [x] **Task 1.2:** Criar o arquivo `docker-compose.yml` na raiz do projeto com o container MySQL 8.0 e testar a subida do banco.
- [x] **Task 1.3:** Configurar as propriedades de conexão com o banco e Flyway no `application.yml`.
- [x] **Task 1.4:** Criar a estrutura de pacotes respeitando a Clean Architecture (`domain`, `application`, `infrastructure`).

---

## 🧬 Fase 2: Camada de Domínio (`domain`) — Core e Regras de Negócio

> ⚠️ *Importante: Esta camada deve conter apenas Java puro, sem anotações do Spring, JPA ou bibliotecas externas.*

- [x] **Task 2.1:** Criar o Enum `ColumnType` com os valores: `INITIAL`, `PENDING`, `FINAL`, `CANCELLED`.
- [x] **Task 2.2:** Criar as entidades de domínio: `Board`, `BoardColumn`, `Card`, `CardColumnHistory` e `CardBlockHistory`.
- [x] **Task 2.3:** Implementar a lógica de criação do `Board` padrão, garantindo que ele nasça automaticamente com as 3 colunas obrigatórias:
  * 1ª: `Backlog` (`INITIAL`)
  * 2ª: `Em andamento` (`PENDING`)
  * 3ª: `Entregue` (`FINAL`)
- [x] **Task 2.4:** Criar métodos de validação no Domínio para regras de posição de colunas:
  * `INITIAL` deve ser sempre a 1ª posição.
  * `FINAL` deve ser sempre a penúltima posição.
  * `CANCELLED` deve ser sempre a última posição.
  * Permitir **N** colunas `PENDING` no meio.
- [x] **Task 2.5:** Implementar a regra de movimentação de `Card`:
  * Mover apenas para a próxima coluna imediatamente à frente.
  * Permitir mover para `CANCELLED` a partir de qualquer coluna (exceto se já estiver em `FINAL`).
  * Impedir movimentação de cards marcados com `is_blocked = true`.
- [x] **Task 2.6:** Implementar a regra de bloqueio/desbloqueio do `Card`:
  * Exigir justificativa obrigatória para bloquear.
  * Registrar timestamps de bloqueio e desbloqueio.
- [x] **Task 2.7:** Criar as exceções customizadas de domínio (`DomainException`, `CardBlockedException`, `InvalidColumnOrderException`, etc.).
- [x] **Task 2.8:** Escrever testes unitários em JUnit 5 testando todas as regras de negócio das entidades de domínio isoladamente.

---

## 🔄 Fase 3: Camada de Aplicação (`application`) — Casos de Uso e Portas

- [x] **Task 3.1:** Criar as interfaces de Portas de Saída (`BoardRepositoryPort`, `ColumnRepositoryPort`, `CardRepositoryPort`, `ReportRepositoryPort`).
- [x] **Task 3.2:** Implementar os DTOs de entrada e saída para as operações do sistema.
- [x] **Task 3.3:** Implementar Caso de Uso: **Gerenciamento de Boards** (Criar, Selecionar, Excluir).
- [x] **Task 3.4:** Implementar Caso de Uso: **Gerenciamento de Colunas** (Criar coluna PENDING, Deletar coluna PENDING, Alterar posição de colunas PENDING).
- [x] **Task 3.5:** Implementar Caso de Uso: **Gerenciamento de Cards** (Criar card, Editar card, Deletar card com validação de campos obrigatórios).
- [x] **Task 3.6:** Implementar Caso de Uso: **Movimentação e Bloqueio** (Mover para próxima coluna, Cancelar card, Bloquear com motivo, Desbloquear card).
- [x] **Task 3.7:** Implementar Caso de Uso: **Relatório de Tempo em Colunas** (Calcular tempo gasto em cada coluna e tempo total até a conclusão/entrega).
- [x] **Task 3.8:** Implementar Caso de Uso: **Relatório de Bloqueios** (Listar histórico de bloqueios, duração de cada bloqueio e justificativa).
- [x] **Task 3.9:** Escrever testes unitários para os Casos de Uso usando Mockito para simular as portas dos repositórios.

---

## 🗄️ Fase 4: Camada de Infraestrutura — Persistência (`infrastructure.persistence`)

- [x] **Task 4.1:** Criar script Flyway `V1__create_board_tables.sql` contendo os schemas de `boards`, `board_columns`, `cards`, `card_column_history` e `card_block_history`.
- [x] **Task 4.2:** Mapear as Entidades JPA (`@Entity`) correspondentes às tabelas do banco de dados.
- [x] **Task 4.3:** Criar as interfaces Spring Data JPA (`BoardJpaRepository`, `CardJpaRepository`, etc.).
- [x] **Task 4.4:** Criar a classe Mappers (`BoardMapper`, `CardMapper`) para conversão de/para Entidades JPA ↔ Entidades de Domínio.
- [x] **Task 4.5:** Implementar os adaptadores concretos de repositório que realizam as operações no banco via Spring Data JPA.

---

## 🖥️ Fase 5: Interface do Usuário (Menu / CLI / REST)

- [x] **Task 5.1:** Implementar o **Menu Principal (Menu 1)** com as opções:
  1. Criar novo board
  2. Selecionar board
  3. Excluir boards
  4. Sair
- [x] **Task 5.2:** Implementar o **Menu do Board Selecionado (Menu 2)** com as opções:
  1. Mover card para próxima coluna
  2. Cancelar card
  3. Criar card
  4. Bloquear card
  5. Desbloquear card
  6. Gerar relatório de tempo
  7. Gerar relatório de bloqueios
  8. Fechar board (Voltar ao Menu Principal)
- [x] **Task 5.3:** Adicionar captura e exibição amigável de erros (validação de campos obrigatórios, erros de movimentação ou regra de negócio).
- [x] **Task 5.4:** Implementar os REST Controllers correspondentes para integrar e expor a API via HTTP.

---

## 🧪 Fase 6: Testes de Integração e Validação Final

- [x] **Task 6.1:** Escrever testes de integração simulando o fluxo completo de um Board:
  * Criar board -> Criar card -> Mover card até Entregue -> Verificar registros na tabela de histórico de tempo.
- [x] **Task 6.2:** Testar o cenário de bloqueio e relatórios:
  * Bloquear card -> Tentar mover (esperar erro) -> Desbloquear -> Verificar relatório de bloqueios gerado.
- [x] **Task 6.3:** Testar regras de ordenação de colunas e exclusão de colunas do tipo `PENDING`.

---

## 📦 Fase 7: Documentação

- [ ] **Task 7.1:** Escrever o `README.md` detalhando como rodar a aplicação, comandos Docker, como executar os testes e exemplos dos menus/relatórios.
- [ ] **Task 7.2:** Fazer o commit final do código revisado e pronto para entrega do bootcamp.