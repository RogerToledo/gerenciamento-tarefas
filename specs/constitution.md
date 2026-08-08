# Constitution / Diretrizes de Governança e Arquitetura do Projeto

Esta "Constitution" estabelece as diretrizes fundamentais de arquitetura, qualidade de código, boas práticas e padrões de projeto para o desenvolvimento do **Board de Tarefas Customizável**.

---

## 🏛️ 1. Princípios de Arquitetura e Design (Clean Architecture & Clean Code)

1. **Clean Architecture Strict (Isolamento de Domínio):**
   * O core do sistema reside na camada `domain/`.
   * A camada `domain/` **não deve possuir nenhuma dependência** de bibliotecas ou frameworks externos (como Spring Framework, JPA/Hibernate, Jackson, etc.). Deve conter apenas Java puro.
   * Regras de negócio, entidades e exceções customizadas pertencem estritamente ao domínio.

2. **Organização em Camadas:**
   * **Domain (`com.bootcamp.app.domain`):** Entidades, Enums, Objetos de Valor e Exceções do Domínio.
   * **Application (`com.bootcamp.app.application`):** Casos de uso (Use Cases), DTOs (Request/Response) e Portas de saída/entrada (Interfaces).
   * **Infrastructure (`com.bootcamp.app.infrastructure`):** Implementações concretas de repositórios (JPA), Mappers, Beans do Spring, Controllers REST e CLI/Menu.

3. **Imutabilidade e Recursos Modernos do Java 25:**
   * Sempre que aplicável para transporte de dados (DTOs, VOs ou dados imutáveis), utilizar **Java Records**.
   * Fazer uso das novidades da linguagem como **Pattern Matching**, **Sealed Classes/Interfaces** e **Switch Expressions** quando aumentarem a legibilidade e a segurança de tipos.

---

## 🔒 2. Regras Invioláveis do Domínio de Negócio

1. **Integridade de Colunas:**
   * Todo novo board deve nascer automaticamente com 3 colunas padrão: **Backlog** (`INITIAL`), **Em andamento** (`PENDING`) e **Entregue** (`FINAL`).
   * A ordenação de colunas deve obrigatoriamente respeitar a estrutura:
     1. Posição 1: Coluna do tipo `INITIAL` (exatamente 1).
     2. Posições Intermediárias: Colunas do tipo `PENDING` (0 a N colunas).
     3. Penúltima Posição: Coluna do tipo `FINAL` (exatamente 1).
     4. Última Posição: Coluna do tipo `CANCELLED` (exatamente 1).

2. **Regras de Movimentação de Cards:**
   * Um card só pode transicionar para a **próxima coluna sequencial imediatamente à frente**.
   * **Exceção de Cancelamento:** O card pode ser transicionado diretamente para a coluna `CANCELLED` de qualquer etapa, exceto se já estiver na coluna `FINAL`.
   * **Bloqueio:** Cards com status `is_blocked = true` **não podem ser movidos** sob nenhuma hipótese.

3. **Rastreabilidade e Métricas:**
   * O bloqueio exige obrigatoriamente uma justificativa (`Motivo do Bloqueio`).
   * Toda transição de coluna e todo evento de bloqueio/desbloqueio deve registrar o timestamp exato (`Data/Hora`) para fins de auditoria e cálculo de métricas de tempo em relatórios.

---

## 🧪 3. Estratégia e Padrões de Teste

1. **Testes Unitários sem Frameworks na Camada de Domínio:**
   * Todas as regras de negócio do Domínio devem ser cobertas por testes unitários rápidos utilizando **JUnit 5**, sem carregar contexto Spring (`@SpringBootTest`).

2. **Testes de Casos de Uso (Camada de Aplicação):**
   * A camada de aplicação deve ser testada unitariamente com **Mockito** simulando as portas/interfaces dos repositórios.

3. **Testes de Integração (Camada de Infraestrutura):**
   * Persistência JPA, mappers e repositórios devem ter testes de integração validados com banco de dados de teste (MySQL via Docker ou banco em memória/Testcontainers).

4. **Regra Zero Regressão:**
   * Nenhuma alteração de código ou refatoração deve ser considerada concluída se houver testes existentes falhando.

---

## 🛠️ 4. Infraestrutura, Persistência e Versionamento de Banco

1. **Migrações com Flyway:**
   * Toda alteração na estrutura do banco de dados relacional (MySQL 8.0) deve ser versionada via scripts SQL do **Flyway** na pasta `src/main/resources/db/migration`.
   * Os scripts devem seguir a convenção de nomes (ex: `V1__create_board_tables.sql`).

2. **Mapeamento e Isolamento de Modelos:**
   * Mapeamentos JPA (`@Entity`) pertencem exclusivamente à camada de infraestrutura (`infrastructure.persistence`).
   * As entidades do domínio não devem conter anotações `@Entity`, `@Table`, `@Column`, etc.
   * Utilizar **Mappers explicitos** para converter `Domain Entity ↔ JPA Entity`.

---

## 📜 5. Convenções de Código e Commits

1. **Nomenclatura Limpa e Autoexplicativa:**
   * Utilizar nomes em inglês para classes, métodos e atributos de código.
   * Mensagens de erro e textos da interface (CLI/Menu) devem ser amigáveis e em Português (pt-BR).

2. **Tratamento Elegante de Exceções:**
   * Nunca engolir exceções ou retornar objetos nulos/vazios em caso de erro silencioso.
   * Lançar exceções de domínio customizadas e tratá-las na borda do sistema (Handlers Globais / CLI Handlers).
