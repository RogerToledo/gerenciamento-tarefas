# 📋 Especificação de Requisitos: Board de Tarefas Customizável

## 1. Visão Geral
Aplicação de gerenciamento e acompanhamento de tarefas no formato de Board (Kanban), permitindo customização de colunas, movimentação sequencial de cards, controle de bloqueios e geração de relatórios de métricas.

---

## 2. Requisitos Funcionais (RF)

### 2.1 Navegação e Menus
* **RF01 - Menu Principal:** O sistema deve iniciar exibindo o menu com as opções:
  1. Criar novo board
  2. Selecionar board
  3. Excluir boards
  4. Sair
* **RF02 - Menu do Board Selecionado:** Ao selecionar um board, o sistema deve disponibilizar:
  1. Visualizar Board (Exibir colunas e cards)  
  2. Mover card para a próxima coluna
  3. Cancelar card
  4. Criar card
  5. Bloquear card
  6. Desbloquear card
  7. Fechar board (voltar ao menu principal)

### 2.2 Gestão de Boards e Colunas
* **RF03 - Estrutura do Board:** Um board possui `Nome` (obrigatório) e `Descrição` (opcional).
* **RF04 - Colunas Padrão Obrigatórias:** Todo novo board deve nascer automaticamente com 3 colunas na seguinte ordem estrita:
  1. **Backlog** (Tipo: `INITIAL`)
  2. **Em andamento** (Tipo: `PENDING`)
  3. **Entregue** (Tipo: `FINAL`)
* **RF05 - Tipos e Regras de Coluna:**
  * Tipos permitidos: `INITIAL`, `PENDING`, `FINAL`, `CANCELLED`.
  * Regra de Quantidade: Exatamente **1** `INITIAL`, **1** `FINAL` e **1** `CANCELLED`. Podem existir **N** colunas `PENDING`.
  * Regra de Posicionamento:
    * `INITIAL`: Sempre a 1ª coluna (posição 1).
    * `FINAL`: Sempre a penúltima coluna.
    * `CANCELLED`: Sempre a última coluna.
* **RF06 - Customização de Colunas:**
  * Permitir criar e deletar colunas do tipo `PENDING`.
  * Permitir reordenar/alterar a posição de colunas do tipo `PENDING`.

### 2.3 Gestão e Movimentação de Cards
* **RF07 - Atributos do Card:** `Título` (obrigatório), `Descrição` (opcional), `Data/Hora de Chegada`, `Data/Hora de Saída`, `Status de Bloqueio` (boolean) e `Motivo do Bloqueio`.
* **RF08 - Fluxo de Movimentação:**
  * O card só pode mover para a **próxima coluna imediatamente à frente** (sem pular etapas).
  * **Exceção de Cancelamento:** Qualquer card (desde que não esteja na coluna `FINAL`) pode ser movido diretamente para a coluna `CANCELLED`.
* **RF09 - Bloqueio e Desbloqueio:**
  * Cards marcados como **bloqueados não podem ser movidos**.
  * Para **bloquear**, é obrigatório fornecer uma justificativa (`Motivo do Bloqueio`).
  * O sistema deve registrar a data/hora exata do bloqueio e do desbloqueio.

### 2.4 Relatórios e Métricas
* **RF10 - Relatório de Tempo em Coluna:** Exibir o tempo total que a tarefa levou para ser concluída, detalhando o tempo de permanência (`Data Chegada` até `Data Saída`) em cada uma das colunas pelas quais passou.
* **RF11 - Relatório de Bloqueios:** Exibir o histórico de bloqueios do board, informando qual card foi bloqueado, a justificativa e a duração total em que permaneceu bloqueado.
* **RF12 - Exibição do Quadro:** O sistema deve listar todas as colunas do board na ordem correta, exibindo dentro de cada coluna seus respectivos cards com ID, Título, Descrição resumida e um indicador visual de bloqueio `[🔒]` acompanhado do motivo, caso esteja bloqueado.
